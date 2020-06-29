package unit.datagenerator;

import org.apache.commons.io.FilenameUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.lanit.at.datagenerator.DataGenerator;
import ru.lanit.at.datagenerator.helpers.FileHelper;

import java.io.File;

public class FileHelperTest {

    private static final int coefficientMbToByte = 1024 * 1024;

    private FileHelper fileHelper;

    @BeforeTest
    public void before() {
        fileHelper = DataGenerator.fileHelper;
    }

    @Test
    public void generate_1byteTxt() {
        // arrange
        double sizeInMB = 0.000001;
        String extension = "txt";

        // act
        File result = fileHelper.generate(sizeInMB, extension);

        // assert
        Assert.assertTrue(result.exists(), "Файл не существует");
        Assert.assertEquals(result.length(), (long) (sizeInMB * coefficientMbToByte), "Размер файла не соответствует ожидаемому");
        Assert.assertEquals(FilenameUtils.getExtension(result.getAbsolutePath()), extension);
    }

    @Test
    public void generate_0byteNumber() {
        // arrange
        double sizeInMB = 0.0;
        String extension = "000";

        // act
        File result = fileHelper.generate(sizeInMB, extension);

        // assert
        Assert.assertTrue(result.exists(), "Файл не существует");
        Assert.assertEquals(result.length(), (long) (sizeInMB * coefficientMbToByte), "Размер файла не соответствует ожидаемому");
        Assert.assertEquals(FilenameUtils.getExtension(result.getAbsolutePath()), extension);
    }

    @Test
    public void generate_1KbEmpty() {
        // arrange
        double sizeInMB = 0.001;
        String extension = "";

        // act
        File result = fileHelper.generate(sizeInMB, extension);

        // assert
        Assert.assertTrue(result.exists(), "Файл не существует");
        Assert.assertEquals(result.length(), (long) (sizeInMB * coefficientMbToByte), "Размер файла не соответствует ожидаемому");
        Assert.assertEquals(FilenameUtils.getExtension(result.getAbsolutePath()), extension);
    }

    @Test(expectedExceptions = NegativeArraySizeException.class)
    public void generate_negativeSizeExe() {
        // arrange
        double sizeInMB = -0.1;
        String extension = "exe";

        // act
        // assert
        fileHelper.generate(sizeInMB, extension);
    }
}