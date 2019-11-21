package utils;


import ru.lanit.at.context.Context;
import ru.lanit.at.make.JSExecutor;

import java.io.File;


public class StyleResourceHelper {
    private static final String STYLES_PATH = "styles";
    private static final String JS_PATH = "js";

    public static void addMouseTracker() {
        String id = "UniqueCssId_MousePointer";
        if (!elementIsExist(id)) {
            injectStyle(StyleResourceEnum.MOUSE_TRACKER, "UniqueCssId_MousePointer");
            Context.getInstance().getBean(JSExecutor.class).executeScript(jsToString(JavaScriptResourceEnum.ADD_MOUSE_TRACKER));
        }
    }

    private static void injectStyle(StyleResourceEnum css, String id) {
        if (!elementIsExist(id)) {
            String cssString = styleToString(css);
            Context.getInstance().getBean(JSExecutor.class).executeScript(jsToString(JavaScriptResourceEnum.INJECT_STYLE), cssString, id);
        }
    }

    public static String styleToString(StyleResourceEnum style, String... params) {
        String path = STYLES_PATH + File.separator + style.value();
        return ResourceFactory.stringFromFileBy(path, params);
    }

    private static String jsToString(JavaScriptResourceEnum script, String... params) {
        String path = JS_PATH + File.separator + script.value();
        return ResourceFactory.stringFromFileBy(path, params);
    }

    private static boolean elementIsExist(String id) {
        return Context.getInstance().getBean(JSExecutor.class).executeScript("return document.getElementById('" + id + "')") != null;
    }
}