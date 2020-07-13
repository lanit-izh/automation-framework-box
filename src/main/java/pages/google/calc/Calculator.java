package pages.google.calc;

import io.qameta.atlas.webdriver.extension.FindBy;
import io.qameta.atlas.webdriver.extension.Param;
import ru.lanit.at.pages.element.UIElement;

public interface Calculator extends UIElement {
    /**
     * This xpath appropriate for most buttons of google calc. But for other keys (like x!) you can invent your own solution
     */
    @FindBy("descendant::div[@role='button'][text()='{{ value }}']")
    UIElement key(@Param("value") String keyText);

    /**
     * This method demonstrates incapsulation of custom UIElements
     *
     * @param keysToSend any charsequences to send
     */
    @Override
    default void sendKeys(CharSequence... keysToSend) {
        for (CharSequence sequence : keysToSend) {
            for (Character key : sequence.toString().toCharArray()) {
                switch (key) {
                    case '/':
                        key("÷").click();
                        break;
                    case '*':
                        key("×").click();
                        break;
                    case '-': // hyphen
                        key("−").click(); // minus sign
                        break;
                    case ' ':
                        continue;
                    default:
                        key(key.toString()).click();
                }
            }
        }
    }
}
