package pages.html_elements;

import io.qameta.atlas.webdriver.extension.FindBy;
import ru.lanit.at.pages.annotations.Title;
import ru.lanit.at.pages.element.UIElement;

@Title(value = "РадиоКнопка")
public interface RadioButton extends UIElement {


    default void select() {
        this.click();
    }


    public interface WithRadioButton extends UIElement {
        @FindBy(".//input[@type='radio']")
        RadioButton radioButton();
    }
}
