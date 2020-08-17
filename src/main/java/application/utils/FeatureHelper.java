package application.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FeatureHelper {
    public static String PATH = "target/test-classes/features/";
    private static Logger logger = LoggerFactory.getLogger(FeatureHelper.class);

    /** Создание  папки с для feature  файла */
    public String createFeaturePath(String uuid) {
        String path = PATH + uuid + "/";
        new File(new File(path).getAbsolutePath()).mkdir();
        return path;
    }


    /** Создание feature файла */
    public void createFeatureFile(String path, String name, String body) throws IOException {
        try (FileOutputStream featureOutputStream = new FileOutputStream(path + name)) {
            featureOutputStream.write(body.getBytes());
        } catch (IOException e) {
            logger.info("Ошибка при сохранении Feature-файла!");
            throw new IOException(e);
        }
    }

    /** Удаление папки с feature файлом */
    public void deleteFeaturePath(String path) {
        logger.info("Удаление папки с feature файлом:" + path);
        if (new File(path).exists()) {
            try {
                Thread.sleep(1000);
                FileUtils.deleteDirectory(new File(path));
            } catch (Exception e) {
                logger.info("Ошибка удаления папки " + path);
                e.printStackTrace();
            }
        }
    }
}