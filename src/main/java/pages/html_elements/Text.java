package pages.html_elements;

import io.qameta.atlas.webdriver.extension.FindBy;
import io.qameta.atlas.webdriver.extension.Param;
import ru.lanit.at.pages.annotations.Title;
import ru.lanit.at.pages.element.UIElement;

@Title(value = "Элемент с текстом")
public interface Text extends UIElement {


    public interface WithText extends UIElement {
        @FindBy(".//*/text()[contains(normalize-space(translate(string(.), '\t\n\r\u00a0', '    ')), '{{ name }}')]/parent::*  ")
        Text text(@Param("name") String name);
    }
}
