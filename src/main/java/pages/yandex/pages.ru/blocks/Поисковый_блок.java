package pages.yandex.pages.ru.blocks;

import io.qameta.atlas.webdriver.extension.FindBy;
import pages.html_elements.Button;
import pages.html_elements.Input;
import ru.lanit.at.pages.block.AbstractBlockElement;


public interface Поисковый_блок extends AbstractBlockElement,
        Input.WithInput, Button.WithButton {

    default void ввести_в_Поле(String name) {
        input().sendKeys(name);
    }

    default void нажать_кнопку_найти() {
        button("Найти").click();
    }

    default void set(String name) {
        input().sendKeys(name);
    }


    interface С_поисковым_блоков extends AbstractBlockElement {
        @FindBy("//*[@aria-label='Поиск в интернете']")
        Поисковый_блок searchBlock();
    }
}