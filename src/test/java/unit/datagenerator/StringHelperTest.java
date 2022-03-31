package unit.datagenerator;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.lanit.at.datagenerator.DataGenerator;
import ru.lanit.at.datagenerator.helpers.StringHelper;

public class StringHelperTest {
    private StringHelper stringHelper;

    @BeforeTest
    public void before() {
        stringHelper = DataGenerator.stringHelper;
    }

    @Test(description = "getRandomCyrillicString возвращает заданное количество заглавных кириллических символов")
    public void getRandomCyrillicString_positive() {
        // arrange
        int count = 10;

        // act
        String result = stringHelper.getRandomCyrillicString(count);

        // assert
        Assert.assertEquals(result.length(), count, "Длина возвращаемой строки не соответствует ожидаемой");
        Assert.assertTrue(result.matches("[А-Я]*"), "В строке есть символы отличные от заглавных кириллических");
    }

    @Test(description = "getRandomCyrillicString возвращает пустую строку")
    public void getRandomCyrillicString_zero() {
        // arrange
        int count = 0;

        // act
        String result = stringHelper.getRandomCyrillicString(count);

        // assert
        Assert.assertTrue(result.isEmpty(), "Строка не пустая");
    }

    @Test(description = "getRandomCyrillicString кидает IllegalArgumentException для отрицательных значений",
            expectedExceptions = IllegalArgumentException.class)
    public void getRandomCyrillicString_negative() {
        // arrange
        int count = -1;

        // act
        // assert
        stringHelper.getRandomCyrillicString(count);
    }

    @Test(description = "getRandomLatinString возвращает заданное количество заглавных латинских символов")
    public void getRandomLatinString_positive() {
        // arrange
        int count = 9;

        // act
        String result = stringHelper.getRandomLatinString(count);

        // assert
        Assert.assertEquals(result.length(), count, "Длина возвращаемой строки не соответствует ожидаемой");
        Assert.assertTrue(result.matches("[A-Z]*"), "В строке есть символы отличные от заглавных латинских");
    }

    @Test(description = "getRandomSpecialSymbol возвращает заданное количество спецсимволов")
    public void getRandomSpecialSymbol_positive() {
        // arrange
        int count = 9;

        // act
        String result = stringHelper.getRandomSpecialSymbol(count);

        // assert
        Assert.assertEquals(result.length(), count, "Длина возвращаемой строки не соответствует ожидаемой");
        Assert.assertTrue(result.matches("[`~!@#$%^&*()-_=+[{]}\\\\;:'\",<.>/?№|»«]*"), "В строке " + result + " есть символы отличные от спецсимволов");
    }

    @Test(description = "getRandomRomanianDigits возвращает заданное количество римских цифр")
    public void getRandomRomanianDigits_positive() {
        // arrange
        int count = 15;

        // act
        String result = stringHelper.getRandomRomanianDigits(count);

        // assert
        Assert.assertEquals(result.length(), count, "Длина возвращаемой строки не соответствует ожидаемой");
        Assert.assertTrue(result.matches("[IXVCMD]*"), "В строке " + result + " есть символы отличные от римских цифр");
    }

    @Test(description = "getRandomNumericNotZero возвращает заданное количество цифр без 0")
    public void getRandomNumericNotZero_positive() {
        // arrange
        int count = 15;

        // act
        String result = stringHelper.getRandomNumericNotZero(count);

        // assert
        Assert.assertEquals(result.length(), count, "Длина возвращаемой строки не соответствует ожидаемой");
        Assert.assertTrue(result.matches("[1-9]*"), "В строке " + result + " есть символы отличные от цифр 1-9");
    }
}