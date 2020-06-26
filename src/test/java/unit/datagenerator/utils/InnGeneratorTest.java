package unit.datagenerator.utils;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.lanit.at.datagenerator.utils.InnGenerator;

public class InnGeneratorTest {
    @Test
    public void generateInn_Fl() {
        // arrange
        // act
        String inn = InnGenerator.generateInn();

        // assert
        Assert.assertTrue(inn.matches("\\d{12}"), "Неправильный формат ИНН: " + inn + ". ИНН для ФЛ должен содержать 12 цифр.");
    }
}