package application.controller;

import application.models.StatusTest;
import application.runner.TestRunner;
import application.utils.ApplicationHelpers;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.redisson.api.RQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.lanit.at.queueservice.dto.TestResult;

import java.io.*;
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

    private RQueue<TestResult> queue;

    @Autowired
    public RunRequestController(RQueue<TestResult> queue) {
        this.queue = queue;
    }

    @RequestMapping(value = "/test",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE + "; charset=utf-8")
    @ApiOperation(value = "Запуск feature файла", consumes = "UUID запуска теста")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "UUID запуска", response = String.class),
            @ApiResponse(code = 500, message = "Ошибка запуска тестов", response = String.class)
    })
    public ResponseEntity<String> runFeature(@RequestParam("id") Long uuid, @RequestParam("testProperties") String testProperties, @RequestParam("feature") MultipartFile feature) {

        LOGGER.info("Запуск теста с идентификатором:" + uuid);
        try {
            String name = feature.getOriginalFilename();
            String body = new String(feature.getBytes(), StandardCharsets.UTF_8);
            File featureFile = File.createTempFile("tst", ".feature");
            //Запуск сценария
            Thread thread = new Thread(() -> {
                long startTime = System.nanoTime();
                try {
                    feature.transferTo(featureFile);
                    ApplicationHelpers.reportHelper.setAllurePath(uuid);
                    ApplicationHelpers.testPropertiesHelper.setTestProperties(testProperties);
                    try {
                        io.cucumber.core.cli.Main.run(new String[]{"--plugin", "pretty", "--plugin", "io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm", "--glue", "steps", "--glue", "hooks", featureFile.getAbsolutePath()}, Thread.currentThread().getContextClassLoader());
                    } catch (Exception e) {}
                } catch (Throwable throwable) {
                    System.out.println(throwable);
                } finally {
                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime) / 1000000000;
                    featureFile.delete();
                    File file;
                    FileInputStream fileInputStream;
                    try {
                        file = new File(ApplicationHelpers.reportHelper.createAllureZipFile(String.valueOf(uuid)));
                        fileInputStream = new FileInputStream(file);
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                        byte[] data = new byte[(int) file.length()];
                        bufferedInputStream.read(data, 0, data.length);
                        TestResult testResult = new TestResult();
                        testResult.setId(uuid);
                        testResult.setLength(duration);
                        testResult.setFile(data);
                        queue.add(testResult);
                        fileInputStream.close();
                        bufferedInputStream.close();
                        ApplicationHelpers.reportHelper.deleteAllurePath(String.valueOf(uuid));
                        ApplicationHelpers.reportHelper.deleteAllureZipFile(String.valueOf(uuid));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        } catch (Exception e) {
            return new ResponseEntity<>("Ошибка : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("UUID:" + uuid, HttpStatus.OK);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE + "; charset=utf-8")
    @ApiOperation(value = "Статус контейнера")
    public ResponseEntity<?> getStatus() {
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Генерация идентификатора запуска
     */
    private String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Обновление статуса прохождения запускаемого теста
     */
    private void updateTestStatus(String uuid) {
        if (failedTestsInfo.get(uuid) == null) {
            tests.put(uuid, StatusTest.COMPLETED);
        } else {
            tests.put(uuid, StatusTest.FAILED);
        }
    }
}
