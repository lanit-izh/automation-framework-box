package pages.yandex.pages;

import pages.html_elements.Button;
import ru.lanit.at.pages.AbstractPage;
import ru.lanit.at.pages.annotations.Title;

@Title(value = "Страница почты")
public interface YandexMailPage extends AbstractPage,
        Button.WithButton {
}