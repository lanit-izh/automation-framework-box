package pages.yandex.pages.ru.blocks;

import io.qameta.atlas.webdriver.extension.FindBy;
import io.qameta.atlas.webdriver.extension.Param;
import pages.html_elements.Link;
import pages.html_elements.Text;
import ru.lanit.at.pages.block.AbstractBlockElement;


public interface Блок_с_результатами_поисковой_выдачи extends AbstractBlockElement,
        Link.WithLink, Text.WithText {

    interface С_блоком_с_результатами_поисковой_выдачи extends AbstractBlockElement {
        @FindBy("//ul[@aria-label='Результаты поиска']//li[{{ value }}]")
        Блок_с_результатами_поисковой_выдачи searchResultBlock(@Param("value") String value);
    }
}