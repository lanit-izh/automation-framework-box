package ru.lanit.at.steps.browser.steps;


import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.И;
import ru.lanit.at.steps.BaseSteps;

public final class CommonStepsBrowserLibrary extends BaseSteps {
    @И("обновить страницу браузера")
    public void clickButtonWithText() {
        getDriver().navigate().refresh();
    }

    @Дано("перейти по адресу {string}")
    public void openPage(String url) {
        getDriver().get(url);
    }
}