package pages.yandex.pages.ru;

import pages.yandex.pages.ru.blocks.Поисковый_блок;
import pages.html_elements.Text;
import ru.lanit.at.pages.AbstractPage;


public interface Поиск_яндекс extends AbstractPage,
        Поисковый_блок.С_поисковым_блоков, Text.WithText {
}