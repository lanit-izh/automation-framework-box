package pages.google;

import io.qameta.atlas.webdriver.extension.FindBy;
import pages.html_elements.Button;
import pages.html_elements.Input;
import ru.lanit.at.pages.AbstractPage;
import ru.lanit.at.pages.annotations.Title;
import ru.lanit.at.pages.annotations.WithName;

@Title("Стартовая страница Google")
public interface GoogleStartPage extends AbstractPage, Button.WithButton {
    @Override
    default boolean isOpen() {
        return searchButton().isDisplayed() && searchField().isDisplayed()
                && getWrappedDriver().getCurrentUrl().matches("https?://(?:www.)?google\\.com/?.*");
    }

    @WithName("Поиск")
    @FindBy("descendant::input[@title='Поиск']")
    Input searchField();

    @WithName("Поиск в Google")
    @FindBy("descendant::input[@value='Поиск в Google'][last()]")
    Button searchButton();

    @WithName("Поиск в Google в выпадающем списке")
    @FindBy("descendant::input[@value='Поиск в Google'][1]")
    Button searchButtonInListBox();
}
