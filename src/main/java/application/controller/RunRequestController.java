package application.controller;

import application.models.TestProperties;
import application.runner.TestRunner;
import com.google.gson.Gson;
import io.qameta.allure.Allure;
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
import java.lang.reflect.Field;
import java.net.URLDecoder;

@RestController
public class RunRequestController {
    private static Logger logger = LoggerFactory.getLogger(RunRequestController.class);
    private static Gson gson = new Gson();

    @RequestMapping(value = "/test",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE + "; charset=utf-8")
    public ResponseEntity<?> create(@RequestParam("testProperties") String testProperties, @RequestParam("feature") MultipartFile feature) {
        try {
            testProperties = URLDecoder.decode(testProperties, "UTF-8");

            TestProperties data = gson.fromJson(testProperties, TestProperties.class);
            createFeatureFile(feature);
            createDefaultProperties(data);
            createAllureProperties();

            //Обнуление пути до папки allure-results
            Class allureClass = Allure.class;
            Field field = allureClass.getDeclaredField("lifecycle");
            field.setAccessible(true);
            field.set(allureClass, null);

            //Запуск сценария
            new Thread(() -> {
                try {
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

    public void createFeatureFile(MultipartFile feature) throws IOException {
        String path = "target/test-classes/features/";
        if (new File(path).exists()) {
            FileUtils.deleteDirectory(new File(path));
        }
        new File(new File(path).getAbsolutePath()).mkdir();
        try (FileOutputStream featureOutputStream = new FileOutputStream(path + feature.getOriginalFilename())) {
            featureOutputStream.write(feature.getBytes());
        } catch (IOException e) {
            logger.info("Ошибка при сохранении Feature-файла!");
            e.printStackTrace();
        }
    }

    public void createDefaultProperties(TestProperties jsonRequest) throws IOException {
        String path = "target/classes/default.properties";

        if (new File(path).exists()) {
            FileUtils.forceDelete(new File(path));
        }

        File defaultProperties = new File(path);
        try (FileOutputStream defaultPropertiesOutputStream = new FileOutputStream(defaultProperties)) {
            String defaultPropertiesFile = "browser=" + jsonRequest.getBrowser() + "\n" +
                    "browser.config=" + jsonRequest.getBrowser_config() + "\n" +
                    "site.url=" + jsonRequest.getSite_url() + "\n" +
                    "remote=" + jsonRequest.getRemote() + "\n" +
                    "hub_url=" + jsonRequest.getHub_url() + "\n" +
                    "proxy=" + jsonRequest.getProxy() +
                    "proxy_config=" + jsonRequest.getProxy_config();

            defaultPropertiesOutputStream.write(defaultPropertiesFile.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAllureProperties() throws IOException {
        String path = "target/test-classes/allure.properties";
        if (new File(path).exists()) {
            FileUtils.forceDelete(new File(path));
        }

        File allureProperties = new File(path);
        try (FileOutputStream allurePropertiesOutputStream = new FileOutputStream(allureProperties)) {
            String allurePropertiesFile = "allure.report.remove.attachments=.*\\\\.png" + "\n" +
                    "allure.results.directory=target/allure-results";
            allurePropertiesOutputStream.write(allurePropertiesFile.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
