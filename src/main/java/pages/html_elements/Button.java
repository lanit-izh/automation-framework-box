package pages.html_elements;

import io.qameta.atlas.webdriver.extension.FindBy;
import io.qameta.atlas.webdriver.extension.Param;
import ru.lanit.at.pages.annotations.Title;
import ru.lanit.at.pages.element.UIElement;


@Title("Кнопка")
public interface Button extends UIElement, Text.WithText {


    interface WithButton extends UIElement {
        @FindBy(".//button")
        Button button();

        @FindBy(".//text()[normalize-space(.) ='Поиск в Google']/ancestor::*[self::button][1]|.//*[contains(@class,'button')and contains(text(),'Поиск в Google')]")
        Button button(@Param("name") String name);
    }
}
