package utils.pdf_test_extractor;

import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


public class TextExtractionStrategyImpl implements TextExtractionStrategy {
    private TreeMap<Float, TreeMap<Float, String>> textMap;


    public TextExtractionStrategyImpl() {
        // reverseOrder используется потому что координата y на странице идет снизу вверх
        textMap = new TreeMap<Float, TreeMap<Float, String>>(Collections.reverseOrder());
    }

    @Override
    public String getResultantText() {
        StringBuilder stringBuilder = new StringBuilder();

        // итерируемся по строкам
        for (Map.Entry<Float, TreeMap<Float, String>> stringMap : textMap.entrySet()) {

            // итерируемся по частям внутри строки
            for (Map.Entry<Float, String> entry : stringMap.getValue().entrySet()) {
                stringBuilder.append(entry.getValue());
            }
            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }

    @Override
    public void beginTextBlock() {
    }

    @Override
    public void renderText(TextRenderInfo renderInfo) {
        // вытаскиваем координаты
        Float x = renderInfo.getBaseline().getStartPoint().get(Vector.I1);
        Float y = renderInfo.getBaseline().getStartPoint().get(Vector.I2);

        // если до этого мы не добавляли элементы из этой строчки файла.
        if (!textMap.containsKey(y)) {
            textMap.put(y, new TreeMap<Float, String>());
        }

        textMap.get(y).put(x, renderInfo.getText());
    }

    @Override
    public void endTextBlock() {
    }

    @Override
    public void renderImage(ImageRenderInfo imageRenderInfo) {
    }

    // метод для извлечения строчек с их y-координатой
    ArrayList<Pair<Float, String>> getStringsWithCoordinates() {
        ArrayList<Pair<Float, String>> result = new ArrayList<Pair<Float, String>>();

        for (Map.Entry<Float, TreeMap<Float, String>> stringMap : textMap.entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<Float, String> entry : stringMap.getValue().entrySet()) {
                stringBuilder.append(entry.getValue());
            }
            result.add(Pair.of(stringMap.getKey(), stringBuilder.toString()));
        }

        return result;
    }
}