package application.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.FileSystemResultsWriter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.allure.AllureHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ReportHelper {
    public static String ALLURE_PATH = "target/allure-results/";
    public static String ALLURE_ZIP_FILE_FORMAT = ALLURE_PATH + "%s" + ".zip";
    private static Logger logger = LoggerFactory.getLogger(ReportHelper.class);

    /** Установка папки для сохранения  allure отчета */
    public void setAllurePath(String uuid) {
        String path = ALLURE_PATH + uuid;
        AllureLifecycle allureLifecycle = new AllureLifecycle(new FileSystemResultsWriter(Paths.get(path)));
        AllureHelper.setLifecycle(allureLifecycle);
        Allure.setLifecycle(allureLifecycle);
    }


    /** Удаление папки с отчетом */
    public void deleteAllurePath(String uuid) {
        String path = ALLURE_PATH + uuid + "/";
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

    /**
     * Архивация папки с аллюр отчетом
     *
     * @return path - адрес нахождения архива с отчетом
     */
    public String createAllureZipFile(String uuid) throws IOException {
        String allurePath = ALLURE_PATH + uuid + "/";
        if (!new File(allurePath).exists()) {
            throw new RuntimeException("не найден сформированный отчет по запуску с uuid:' " + uuid + "'. Проверьте статус запуска теста");
        }

        String zipPath = String.format(ALLURE_ZIP_FILE_FORMAT, uuid);
        File f = new File(zipPath);
        if (f.exists() && !f.isDirectory()) {
            return zipPath;
        } else {
            FileOutputStream fos = new FileOutputStream(zipPath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File fileToZip = new File(allurePath);
            zipFile(fileToZip, fileToZip.getName(), zipOut);
            zipOut.close();
            fos.close();
            return zipPath;
        }
    }


    /** Удаление файла архива с аллюр отчетом */
    public void deleteAllureZipFile(String uuid) {
        String zipPath = String.format(ALLURE_ZIP_FILE_FORMAT, uuid);
        File f = new File(zipPath);
        if (f.exists() && !f.isDirectory()) {
            FileUtils.deleteQuietly(new File(zipPath));
        }
    }


    /** Архивирование файлов в папке */
    private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

}
