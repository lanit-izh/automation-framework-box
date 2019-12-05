package steps.browser.steps;


import cucumber.api.java.ru.И;
import steps.BaseSteps;

public final class CommonStepsBrowserLibrary extends BaseSteps {
    @И("обновить страницу браузера")
    public void clickButtonWithText() {
        getDriver().navigate().refresh();
    }
}