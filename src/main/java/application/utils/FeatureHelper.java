package application.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FeatureHelper {
    public static String PATH = "features/";
    private static Logger logger = LoggerFactory.getLogger(FeatureHelper.class);

    /** Создание  папки с для feature  файла */
    public String createFeaturePath(Long uuid) {
        String path = PATH + uuid + "/";
        new File(new File(path).getAbsolutePath()).mkdirs();
        return path;
    }
}