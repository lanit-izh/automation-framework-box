package pages.html_elements;

import io.qameta.atlas.webdriver.extension.FindBy;
import io.qameta.atlas.webdriver.extension.Param;
import ru.lanit.at.pages.annotations.Title;
import ru.lanit.at.pages.element.UIElement;


@Title(value = "Кнопка")
public interface Button extends UIElement, Text.WithText {


    public interface WithButton extends UIElement {
        @FindBy(".//button")
        Button button();

        @FindBy(".//text()[normalize-space(.) ='{{ name }}']/ancestor::*[self::button][1]|.//*[contains(@class,'button')and contains(text(),'{{ name }}')]")
        Button button(@Param("name") String name);
    }
}
