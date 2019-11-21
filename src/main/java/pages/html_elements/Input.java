package pages.html_elements;

import io.qameta.atlas.core.api.Retry;
import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.ElementsCollection;
import io.qameta.atlas.webdriver.extension.FindBy;
import io.qameta.atlas.webdriver.extension.Param;
import org.openqa.selenium.NoSuchElementException;
import ru.lanit.at.pages.annotations.Title;
import ru.lanit.at.pages.element.UIElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@Title(value = "Поле ввода")
public interface Input extends UIElement {


    default void sendKeys(LocalDate localDate) {
        sendKeys(localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    public interface WithInput extends UIElement {
        @FindBy(".//input")
        ElementsCollection<Input> inputs();

        @Retry(timeout = 21000, ignoring = NoSuchElementException.class)
        @FindBy(".//label[contains(text(),'{{ value }}')]/preceding-sibling::span/input")
        Input input_label(@Param("value") String value);

        default Input input() {
            Input i = inputs().filter(AtlasWebElement::isDisplayed).waitUntil(hasSize(greaterThan(0))).get(0);
            return i;
        }

    }
}
