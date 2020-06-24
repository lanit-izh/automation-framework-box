package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.lanit.at.context.Context;
import ru.lanit.at.exceptions.FrameworkRuntimeException;
import ru.lanit.at.make.JSExecutor;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.lang.Thread.sleep;

public class Wait {

    private static final int DEFAULT_TIMEOUT_SEC = 5;
    private static final int CHECK_STATE_PERIOD_MS = 200;

    static Logger log = LogManager.getLogger(Wait.class);
    private JSExecutor jsExecutor = Context.getInstance().getBean(JSExecutor.class);

    public static void sec(int seconds) {
        try {
            sleep(1000 * seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sec(double seconds) {
        try {
            sleep((long) (1000 * seconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void until(Supplier<Boolean> waitingCondition) {
        until(waitingCondition, DEFAULT_TIMEOUT_SEC);
    }

    public static void until(Supplier<Boolean> waitingCondition, int timeout) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (long) (timeout * 1000);
        while (!waitingCondition.get() && System.currentTimeMillis() < endTime) {
            try {
                sleep(CHECK_STATE_PERIOD_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long processTime = (System.currentTimeMillis() - startTime) / 1000;
        if (processTime > 1) log.trace("Ожидание длилось {} сек", processTime);
    }


    /**
     * То же, что и {@link #until(Object, Predicate, double)}, но с таймаутом по-умолчанию {@link #DEFAULT_TIMEOUT_SEC}
     */
    public static <T> void until(T obj, Predicate<T> predicate) {
        until(obj, predicate, DEFAULT_TIMEOUT_SEC);
    }

    /**
     * <p>Ожидает, пока указанная функция не начнёт возвращать {@code true} (т.е. выполняется пока результат {@code false}), либо конца таймаута. Например, чтобы подождать появления элемента, вызываем:</p><br/>
     * <p><code>until(webElement, element -> element.isDisplayed(), 20)<br/>// ждёт в течение 20 сек, что элемент появится</code></p>
     *
     * @param obj       объект, над которым производится циклическая проверка
     * @param predicate функция, которая выполняет проверку объекта.
     * @param timeout   таймаут проверки в секундах
     */
    public static  <T> void until(T obj, Predicate<T> predicate, double timeout) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (long) (timeout * 1000);
        while (!predicate.test(obj) && System.currentTimeMillis() < endTime) {
            try {
                sleep(CHECK_STATE_PERIOD_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long processTime = (System.currentTimeMillis() - startTime) / 1000;
        if (processTime > 1) log.trace("Ожидание длилось {} сек", processTime);
    }

    public static void untilOrException(Supplier<Boolean> waitingCondition, String exceptionMessage) {
        untilOrException(waitingCondition, DEFAULT_TIMEOUT_SEC, exceptionMessage);
    }


    public static void untilOrException(Supplier<Boolean> waitingCondition, int timeout, String exceptionMessage) {
        until(waitingCondition, timeout);
        if (!waitingCondition.get()) throw new FrameworkRuntimeException(exceptionMessage);
    }

    /**
     * Аналогично {@link #until(Object, Predicate, double)}, но если ожидание закончится бросит эксепшен
     *
     * @throws FrameworkRuntimeException если ожидание кончится, а условие не выполнится
     */
    public static <T> void untilOrException(T obj, Predicate<T> predicate, double timeout, String errorMessage) {
        until(obj, predicate, timeout);
        if (!predicate.test(obj))
            throw new FrameworkRuntimeException(errorMessage + " Время ожидания: " + timeout + " секунд");
    }

    public boolean isPageLoaded() {
        return this.jsExecutor.executeScript("return document.readyState", new Object[0]).equals("complete");
    }

    public boolean isPageInteractive() {
        return this.isPageLoaded() || this.jsExecutor.executeScript("return document.readyState", new Object[0]).equals("interactive");
    }

    public boolean isJSActive() {
        try {
            return !this.jsExecutor.executeScript("return jQuery.active", new Object[0]).toString().equals("0");
        } catch (Exception var2) {
            return false;
        }
    }

    public void untilJSComplete() {
        this.until(() -> {
            return !this.isJSActive();
        }, 60);
        if (this.isJSActive()) {
            this.log.error("JavaScript (jQuery) выполнялся слишком долго");
        }
    }

    public void untilPageLoaded() {
        log.debug("Ожидаем загрузки страницы... \t");
        until(this::isPageLoaded, 30);
        if (!this.isPageLoaded()) {
            this.log.warn("Страница загружалась слишком долго, какие-то элементы могут быть недоступны");
        }
    }
}
