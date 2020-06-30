package unit.datagenerator;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.lanit.at.datagenerator.DataGenerator;
import ru.lanit.at.datagenerator.helpers.PersonalData;

public class PersonalDataTest {
    @Test
    public void getSnilsTest() {
        // arrange
        PersonalData personalData = DataGenerator.personalData;

        // act
        String snils = personalData.getSnils();

        // assert
        Assert.assertTrue(snils.matches("[0-9]{3}-[0-9]{3}-[0-9]{3} [0-9]{2}"), "Неправильный формат СНИЛС");
    }
}