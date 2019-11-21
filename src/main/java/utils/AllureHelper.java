package utils;


import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

public class AllureHelper {
    private static final Logger LOG = LogManager.getLogger(AllureHelper.class);

    public static void attachScreenShot(String name, byte[] bytes) {
        Allure.getLifecycle().addAttachment(name, "image/png", "png", bytes);
    }

    public static void attachTxt(String name, String text) {
        Allure.getLifecycle().addAttachment(name, "text/plain", "txt", text.getBytes(StandardCharsets.UTF_8));
    }

    public static void attachPageSource(byte[] bytes) {
        Allure.getLifecycle().addAttachment("Page source", "text/html", "html", bytes);
    }
}