package application.controller;

import application.models.TestProperties;
import application.runner.TestRunner;
import com.google.gson.Gson;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.FileSystemResultsWriter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

@RestController
public class RunRequestController {
    private static final int SEC = 1000;

    private static Logger logger = LoggerFactory.getLogger(RunRequestController.class);
    private static Gson gson = new Gson();
    private static Semaphore semaphore = new Semaphore(1);

    @RequestMapping(value = "/test",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE + "; charset=utf-8")
    public ResponseEntity<?> create(@RequestParam("testProperties") String testProperties, @RequestParam("feature") MultipartFile feature) {
        try {
            String name = feature.getOriginalFilename();
            String body = new String(feature.getBytes(), StandardCharsets.UTF_8);

            testProperties = URLDecoder.decode(testProperties, "UTF-8");

            TestProperties data = gson.fromJson(testProperties, TestProperties.class);

            //Запуск сценария
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    sleep(5 * SEC);

                    //Обнуление пути до папки allure-results
                    String path = "target/allure-results";
                    Allure.setLifecycle(new AllureLifecycle(new FileSystemResultsWriter(Paths.get(path))));

                    setTestProperties(data);
                    createFeatureFile(name, body);

                    semaphore.release();
                    new TestRunner().runScenario();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            return new ResponseEntity<>("Ошибка: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Тест запущен успешно!", HttpStatus.OK);
    }

    public void createFeatureFile(String name, String body) throws IOException {
        String path = "target/test-classes/features/";
        if (new File(path).exists()) {
            FileUtils.deleteDirectory(new File(path));
        }
        new File(new File(path).getAbsolutePath()).mkdir();
        try (FileOutputStream featureOutputStream = new FileOutputStream(path + name)) {
            featureOutputStream.write(body.getBytes());
        } catch (IOException e) {
            logger.info("Ошибка при сохранении Feature-файла!");
            e.printStackTrace();
        }
    }

    private void setTestProperties(TestProperties jsonRequest) {
        System.setProperty("browser", jsonRequest.getBrowser());
        System.setProperty("browser.config", jsonRequest.getBrowser_config());
        System.setProperty("site.url", jsonRequest.getSite_url());
        System.setProperty("remote", jsonRequest.getRemote());
        System.setProperty("hub_url", jsonRequest.getHub_url());
        System.setProperty("proxy", jsonRequest.getProxy());
        System.setProperty("proxy_config", jsonRequest.getProxy_config());
    }
}
