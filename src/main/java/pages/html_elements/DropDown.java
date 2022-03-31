package pages.html_elements;

import io.qameta.atlas.webdriver.extension.FindBy;
import ru.lanit.at.pages.annotations.Title;
import ru.lanit.at.pages.element.UIElement;

@Title("ДропДаун")
public interface DropDown extends UIElement {


    default void selectByValue(String value) {
        this.sendKeys(value);
    }

    default void selectByIndex(int index) {
        this.sendKeys();
    }


    default void selectMultipleItemsInDropdown(String... selectedLabels) {
        sendKeys(selectedLabels);
    }

    default String getSelectedInDropdownValue() {
        return getText();
    }


    interface WithDropDown extends UIElement {
        @FindBy(".//select")
        DropDown dropDown();
    }
}
