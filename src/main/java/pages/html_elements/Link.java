package pages.html_elements;

import io.qameta.atlas.webdriver.extension.FindBy;
import ru.lanit.at.pages.annotations.Title;
import ru.lanit.at.pages.element.UIElement;

@Title(value = "Ссылка")
public interface Link extends UIElement {


    default String getLinkAddress() {
        return getAttribute("href");
    }


    public interface WithLink extends UIElement {
        @FindBy(".//a")
        Link link();
    }
}
