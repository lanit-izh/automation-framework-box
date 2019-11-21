package pages.yandex.pages.blocks;

import io.qameta.atlas.webdriver.extension.FindBy;
import io.qameta.atlas.webdriver.extension.Param;
import pages.html_elements.Link;
import pages.html_elements.Text;
import ru.lanit.at.pages.annotations.Title;
import ru.lanit.at.pages.block.AbstractBlockElement;

@Title(value = "Результат поисковой выдачи")
public interface SearchResultBlock extends AbstractBlockElement,
        Link.WithLink, Text.WithText {
    interface WithSearchResultBlock extends AbstractBlockElement {
        @FindBy("//ul[@aria-label='Результаты поиска']//li[{{ value }}]")
        SearchResultBlock searchResultBlock(@Param("value") String value);
    }
}

