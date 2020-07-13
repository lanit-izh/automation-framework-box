package unit.pdf_text_extractor;//import javafx.util.Pair;

import org.apache.commons.lang3.tuple.Pair;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.pdf_test_extractor.TextExtractor;

import java.io.IOException;
import java.util.ArrayList;

/** Юнит тест для проверка содержимого pdf файла */
public class PdfTest {

    @Test
    public void pdfExtractorTest() throws IOException {
        ArrayList<Pair<Float, String>> pdf = new TextExtractor().getArrayListOfPdf("src\\test\\resources\\unit\\files\\TestFile.pdf");
        for (Pair<Float, String> pair : pdf) {
            System.out.println(pair.getKey().toString() + " " + pair.getValue());
        }
        ArrayList<Pair<String, String>> values = new ArrayList<Pair<String, String>>();
        values.add(Pair.of("Фамилия заявителя", "Иванов"));
        values.add(Pair.of("Фамилияqw", "Тfewестов"));
        values.add(Pair.of("Имя заявителя", "Иван"));
        values.add(Pair.of("Отчество заявителя", "Иванович"));
        Assert.assertTrue(new TextExtractor().pdfComparison(pdf, values.get(0), ":"), "Не правильная фамилия заявителя");
        Assert.assertFalse(new TextExtractor().pdfComparison(pdf, values.get(1), ":"), "Присутствует не верное значение Фамилияqw");
        Assert.assertTrue(new TextExtractor().pdfComparison(pdf, values.get(2), ":"), "Не правильное имя заявителя");
        Assert.assertTrue(new TextExtractor().pdfComparison(pdf, values.get(3), ":"), "Не правильное отчество заявителя");
    }
}