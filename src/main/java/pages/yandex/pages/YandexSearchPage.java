package pages.yandex.pages;

import pages.html_elements.Input;
import pages.html_elements.Text;
import pages.yandex.pages.blocks.SearchPageBlock;
import ru.lanit.at.pages.AbstractPage;
import ru.lanit.at.pages.annotations.Title;

@Title("Поиск яндекс")
public interface YandexSearchPage extends AbstractPage,
        SearchPageBlock.WithSearchPageBlock, Text.WithText, Input.WithInput {

}






