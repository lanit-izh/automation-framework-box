package pages.google.calc;

import io.qameta.atlas.webdriver.extension.FindBy;
import io.qameta.atlas.webdriver.extension.Selector;
import pages.html_elements.Button;
import ru.lanit.at.pages.AbstractPage;
import ru.lanit.at.pages.annotations.Title;
import ru.lanit.at.pages.annotations.WithName;
import ru.lanit.at.pages.element.UIElement;

@Title("Результат поиска с калькулятором")
public interface GoogleResultPageWithCalc extends AbstractPage {
    @Override
    default boolean isOpen() {
        return searchResults().isDisplayed() && calc().isDisplayed();
    }

    @WithName("Калькулятор")
    @FindBy(".//h2[text()='Калькулятор']/following-sibling::div")
    Calculator calc();

    @FindBy("//div[@id='search']")
    UIElement searchResults();

    @WithName("Очистить")
    @FindBy(".//div[@role='button'][.='AC']")
    Button clearBtn();

    @WithName("Результат")
    @FindBy(value = "cwos", selector = Selector.ID)
    UIElement resultField();
}
