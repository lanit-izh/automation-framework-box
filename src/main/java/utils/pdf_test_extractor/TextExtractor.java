package utils.pdf_test_extractor;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.ArrayList;

//import javafx.util.Pair;

public class TextExtractor {
    public ArrayList<Pair<Float, String>> getArrayListOfPdf(String filePath) throws IOException {
        PdfReader reader = new PdfReader(filePath);
        ArrayList<Pair<Float, String>> allPdf = new ArrayList<Pair<Float, String>>();
        for (int i = 1; i <= reader.getNumberOfPages(); ++i) {
            TextExtractionStrategyImpl strategy = new TextExtractionStrategyImpl();

            // вызываем, чтобы наша реализация стратегия получила информацию о тексте на странице
            PdfTextExtractor.getTextFromPage(reader, i, strategy);

            allPdf.addAll(strategy.getStringsWithCoordinates());
        }

        for (int i = 0; i < allPdf.size(); i++) {
            if (allPdf.get(i).getValue().indexOf(' ') == 0) {
                allPdf.set(i, Pair.of(allPdf.get(i).getKey(), allPdf.get(i).getValue().substring(1)));
            }
        }

        reader.close();
        return allPdf;
    }

    /**
     * @param pdf       Лист полей из пдф
     * @param elm       Пара сверяемых полей , где ключ - название поля , а значение - значение поля
     * @param delimiter разделитель , который есть между полем и его значением. Если такого нет , то пердать пустую стрингу
     * @return boolean
     */
    public boolean pdfComparison(ArrayList<Pair<Float, String>> pdf, Pair<String, String> elm, String delimiter) {
        boolean result = false;
        for (int i = 0; i < pdf.size(); i++) {
            if (pdf.get(i).getValue().contains(elm.getKey())) {//Проверяется наличие поля
                if (pdf.get(i).getValue().contains(elm.getValue())) {//Проверяется наличие значения
                    if (pdf.get(i).getValue().substring(pdf.get(i).getValue().indexOf(elm.getKey()), pdf.get(i).getValue().lastIndexOf(elm.getValue())).replace(delimiter, "").replace(" ", "").equals(elm.getKey().replace(" ", ""))) {//Проверка названия поля
                        if (pdf.get(i).getValue().substring(pdf.get(i).getValue().lastIndexOf(elm.getValue())).trim().equals(elm.getValue())) { //Проверка значения поля
                            result = true;
                        }
                    }
                } else if ((pdf.get(i).getValue().length() - elm.getKey().length()) < elm.getValue().length()) {
                    int k = i;
                    ArrayList<Pair<Float, String>> newPdfLine = (ArrayList<Pair<Float, String>>) pdf.clone();
                    while ((newPdfLine.get(i).getValue().length() - elm.getKey().length()) < elm.getValue().length()) {// начинаем прибавлять послеущие строки, чтобы значение сложилась в одну строку
                        newPdfLine.set(i, Pair.of(newPdfLine.get(i).getKey(), newPdfLine.get(i).getValue().trim() + " " + newPdfLine.get(k + 1).getValue().trim()));
                        k++;
                    }
                    if (newPdfLine.get(i).getValue().substring(pdf.get(i).getValue().indexOf(elm.getKey())).replace(elm.getKey(), "").contains(elm.getValue())) { //Проверка значения поля
                        result = true;
                    }
                }
            } else if (pdf.get(i).getValue().length() < elm.getKey().length()) {
                int k = i;
                ArrayList<Pair<Float, String>> newPdfLine = (ArrayList<Pair<Float, String>>) pdf.clone();
                while ((newPdfLine.get(i).getValue().length() < (elm.getKey().length() + elm.getValue().length())) && (i + 1 < newPdfLine.size())) {// начинаем прибавлять послеущие строки, чтобы название поля сложилось в одну строку
                    if (k < newPdfLine.size() - 1) {
                        newPdfLine.set(i, Pair.of(newPdfLine.get(i).getKey(), newPdfLine.get(i).getValue().trim() + " " + newPdfLine.get(k + 1).getValue().trim()));
                        k++;
                    } else {
                        break;
                    }
                }
                if (newPdfLine.get(i).getValue().contains(elm.getValue()) && newPdfLine.get(i).getValue().contains(elm.getKey())) { //Проверка значения поля
                    result = true;

                }
            }
        }
        if (!result) {
            return false;
        }
        return result;
    }
}