package pages.yandex.pages;

import pages.yandex.pages.blocks.SearchResultBlock;
import ru.lanit.at.pages.AbstractPage;
import ru.lanit.at.pages.annotations.Title;

@Title("Результаты поиска")
public interface YandexSearchResultPage extends AbstractPage,
        SearchResultBlock.WithSearchResultBlock {
}



