package application.controller;

import application.runner.TestRunner;
import application.utils.ApplicationHelpers;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import static application.utils.ReportHelper.ALLURE_PATH;
import static java.lang.Thread.sleep;

@RestController
public class RunRequestController {
    private static final int SEC = 1000;

    private static Logger LOGGER = LoggerFactory.getLogger(RunRequestController.class);
    private static Semaphore semaphore = new Semaphore(1);
    private Map<String, StatusTest> tests = new ConcurrentHashMap<>();
    private Map<String, String> failedTestsInfo = new ConcurrentHashMap<>();


    @RequestMapping(value = "/test",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE + "; charset=utf-8")
    @ApiOperation(value = "Запуск feature файла", consumes = "UUID запуска теста")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "UUID запуска", response = String.class),
            @ApiResponse(code = 500, message = "Ошибка запуска тестов", response = String.class)
    })
    public ResponseEntity<String> runFeature(@RequestParam("testProperties") String testProperties, @RequestParam("feature") MultipartFile feature) {

        String uuid = generateUUID();
        LOGGER.info("Запуск теста с идентификатором:" + uuid);
        try {
            String name = feature.getOriginalFilename();
            String body = new String(feature.getBytes(), StandardCharsets.UTF_8);
            String featurePath = ApplicationHelpers.featureHelper.createFeaturePath(uuid);

            //Запуск сценария
            Thread thread = new Thread(() -> {
                try {
                    semaphore.acquire();
                    sleep(5 * SEC);
                    try {
                        ApplicationHelpers.reportHelper.setAllurePath(uuid);
                        ApplicationHelpers.testPropertiesHelper.setTestProperties(testProperties);
                        ApplicationHelpers.featureHelper.createFeatureFile(featurePath, name, body);
                    } catch (Throwable throwable) {
                        failedTestsInfo.put(uuid, throwable.getMessage());
                        throw new Throwable(throwable);
                    }
                    semaphore.release();
                    tests.put(uuid, StatusTest.IN_PROGRESS);
                    new TestRunner(featurePath).runScenario();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    if (semaphore.availablePermits() == 0) semaphore.release();
                } finally {
                    updateTestStatus(uuid);
                    ApplicationHelpers.featureHelper.deleteFeaturePath(featurePath);
                }
            });

            thread.start();
        } catch (Exception e) {
            return new ResponseEntity<>("Ошибка : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("UUID:" + uuid, HttpStatus.OK);
    }


    @RequestMapping(value = "/status/{uuid}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE + "; charset=utf-8")
    @ApiOperation(value = "Получение информации о статусе прохождения теста")
    public ResponseEntity<?> getStatusTest(@PathVariable String uuid) {
        if (tests.get(uuid) == null) {
            return new ResponseEntity<>("Ошибка: Тест идентификатором  '" + uuid + "' не найден", HttpStatus.BAD_REQUEST);
        }
        if (tests.get(uuid) == StatusTest.FAILED) {
            return new ResponseEntity<>("Произошла ошибка при попытке запуска теста. " + failedTestsInfo.get(uuid), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(tests.get(uuid).name(), HttpStatus.OK);
    }

    @RequestMapping(value = "allure/download/{uuid}", method = RequestMethod.GET)
    @ApiOperation(value = "Скачивание zip архива с отчетом Allure")
    public ResponseEntity<?> download(@PathVariable String uuid) throws IOException {
        if (tests.get(uuid) == null) {
            return new ResponseEntity<>("Ошибка: Тест идентификатором  '" + uuid + "' не найден", HttpStatus.BAD_REQUEST);
        }
        if (tests.get(uuid) != StatusTest.COMPLETED) {
            return new ResponseEntity<>("Ошибка: Тест идентификатором  '" + uuid + "' не завершил выполнение, формирование отчета невозможно", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Content-Disposition", "attachment; filename=\"" + uuid + ".zip\"");
        InputStream is = new FileInputStream(new File(ApplicationHelpers.reportHelper.createAllureZipFile(uuid)));
        Resource resource = new InputStreamResource(is);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "allure/delete/{uuid}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE + "; charset=utf-8")
    @ApiOperation(value = "Удаление данных  Allure отчета")
    public ResponseEntity<?> deleteAllure(@PathVariable String uuid) {
        if (!new File(ALLURE_PATH + uuid + "/").exists()) {
            return new ResponseEntity<>("Allure отчет теста с UUID:'" + uuid + "', не найден", HttpStatus.OK);
        }
        if (tests.get(uuid) != StatusTest.COMPLETED) {
            return new ResponseEntity<>("Ошибка: Тест идентификатором  '" + uuid + "' не завершил выполнение, попробуйте удалить отчет позднее", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ApplicationHelpers.reportHelper.deleteAllurePath(uuid);
        ApplicationHelpers.reportHelper.deleteAllureZipFile(uuid);
        return new ResponseEntity<>("Allure отчет теста с UUID:'" + uuid + "', успешно удален", HttpStatus.OK);
    }

    /** Генерация идентификатора запуска */
    private String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /** Обновление статуса прохождения запускаемого теста */
    private void updateTestStatus(String uuid) {
        if (failedTestsInfo.get(uuid) == null) {
            tests.put(uuid, StatusTest.COMPLETED);
        } else {
            tests.put(uuid, StatusTest.FAILED);
        }
    }


    enum StatusTest {
        COMPLETED, IN_PROGRESS, FAILED
    }
}
