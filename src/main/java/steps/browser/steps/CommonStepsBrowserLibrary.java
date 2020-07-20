package steps.browser.steps;


import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.И;
import steps.BaseSteps;
import utils.data.helpers.DataRandomGenerator;


public final class CommonStepsBrowserLibrary extends BaseSteps {
    @И("обновить страницу браузера")
    public void clickButtonWithText() {
        getDriver().navigate().refresh();
    }

    @Дано("перейти по адресу {string}")
    public void openPage(String url) {
        getDriver().get(DataRandomGenerator.replaceAllGeneratingValues(url));
    }
}