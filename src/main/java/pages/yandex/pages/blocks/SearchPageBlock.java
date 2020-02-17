package pages.yandex.pages.blocks;

import io.qameta.atlas.webdriver.extension.FindBy;
import pages.html_elements.Button;
import pages.html_elements.Input;
import ru.lanit.at.pages.annotations.Title;
import ru.lanit.at.pages.annotations.WithName;
import ru.lanit.at.pages.block.AbstractBlockElement;

@Title(value = "Блок поиска")
public interface SearchPageBlock extends AbstractBlockElement,
        Input.WithInput, Button.WithButton {

    @WithName("Найти")
    @FindBy(".//button[.='Найти']")
    Button findButton();

    interface WithSearchPageBlock extends AbstractBlockElement {
        @FindBy("//*[@aria-label='Поиск в интернете']")
        SearchPageBlock searchBlock();
    }
}
