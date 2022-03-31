package utils.allure;


import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

public class AllureHelper {
    private static final Logger LOG = LogManager.getLogger(AllureHelper.class);
    private static ThreadLocal<AllureLifecycle> threadLocal = new ThreadLocal<>();

    /* Установка  AllureLifecycle для потока выполнения, используется при запуске фреймворка, как веб сервиса **/
    public static void setLifecycle(AllureLifecycle allureLifecycle) {
        threadLocal.set(allureLifecycle);
    }

    public static void attachScreenShot(String name, byte[] bytes) {
        get().addAttachment(name, "image/png", "png", bytes);
    }

    public static void attachTxt(String name, String text) {
        get().addAttachment(name, "text/plain", "txt", text.getBytes(StandardCharsets.UTF_8));
    }

    public static void attachPageSource(byte[] bytes) {
        get().addAttachment("Page source", "text/html", "html", bytes);
    }

    public static void setStepStatusBroken(String description) {
        get().updateStep(stepResult -> stepResult.setDescription(description));
    }


    private static AllureLifecycle get() {
        if (threadLocal.get() == null) {
            return Allure.getLifecycle();
        }
        return threadLocal.get();
    }
}