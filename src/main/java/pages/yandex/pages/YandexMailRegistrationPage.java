package pages.yandex.pages;

import pages.html_elements.Button;
import pages.html_elements.Input;
import ru.lanit.at.pages.AbstractPage;
import ru.lanit.at.pages.annotations.Title;

@Title(value = "Регистрация почты")
public interface YandexMailRegistrationPage extends AbstractPage,
        Input.WithInput, Button.WithButton {
}