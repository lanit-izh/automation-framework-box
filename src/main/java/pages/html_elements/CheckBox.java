package pages.html_elements;

import io.qameta.atlas.webdriver.extension.FindBy;
import ru.lanit.at.pages.annotations.Title;
import ru.lanit.at.pages.element.UIElement;

@Title("Чекбокс")
public interface CheckBox extends UIElement {


    default boolean isChecked() {
        return this.isSelected();
    }

    default void setChecked(boolean checked) {
        if (checked) {
            if (!isChecked()) {
                this.click();
            }
        } else {
            if (isChecked()) {
                this.click();
            }
        }
    }


    interface WithCheckBox extends UIElement {
        @FindBy(".//input[@type='checkbox']")
        CheckBox checkBox();
    }
}
