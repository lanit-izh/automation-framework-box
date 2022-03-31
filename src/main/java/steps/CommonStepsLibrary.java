package steps;


import cucumber.api.java.ru.Если;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.То;
import cucumber.api.java.ru.Тогда;
import io.cucumber.datatable.DataTable;
import io.qameta.atlas.webdriver.AtlasWebElement;
import org.openqa.selenium.WebDriver;
import pages.html_elements.Button;
import pages.html_elements.CheckBox;
import pages.html_elements.DropDown;
import pages.html_elements.Input;
import pages.html_elements.Link;
import pages.html_elements.RadioButton;
import pages.html_elements.Text;
import ru.lanit.at.exceptions.FrameworkRuntimeException;
import ru.lanit.at.pages.AbstractPage;
import ru.lanit.at.pages.element.UIElement;
import ru.yandex.qatools.matchers.webdriver.EnabledMatcher;
import utils.data.helpers.DataRandomGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.not;
import static ru.yandex.qatools.matchers.webdriver.DisplayedMatcher.displayed;

public final class CommonStepsLibrary extends BaseSteps {


    @И("^перейти к странице '(.*)'$")
    public void changePageTo(String pageName) {
        getPageByTitle(pageName);
        resetCurrentBlock();
    }

    @И("^вернуться к странице$")
    public void returnToPage() {
        resetCurrentBlock();
    }

    @И("проверить, что тайтл страницы = {string}")
    public void checkPageTitle(String expectedTitle) {
        String actualTitle = getCurrentPage().getWrappedDriver().getTitle();
        softAssert().assertTrue(expectedTitle.equalsIgnoreCase(actualTitle), "Ожидалось что тайтл страницы '" + expectedTitle + "', фактически:'" + actualTitle + "'");
    }


    @И("нажать на {element}")
    public void clickOnElem(UIElement element) {
        element.click();
    }

    @И("нажать на {type} с текстом {string}")
    public void clickButtonWithText(Class<? extends UIElement> type, String text) {
        getUIElement(type, text).click();
    }

    @И("нажать на {type} {string}")
    public void clickByName(Class<? extends UIElement> type, String elementName) {
        getElementByName(elementName, type).click();
    }

    @И("^нажать с помощью JS на кнопку с текстом '(.*)'$")
    public void clickJsButtonWithText(String param) {
        UIElement uiElement = getUIElement(Button.class, param);
        uiElement.make().jsClickOn(uiElement);
    }


    @И("ввести в поле {element} значение {string}")
    public void typeIntoInput(UIElement element, String text) {
        element.sendKeys(text);
    }

    @И("ввести в поля ввода значения")
    public void typeIntoInput(DataTable table) {
        List<Map<String, String>> list = table.asMaps(String.class, String.class);
        for (Map<String, String> l : list) {
            String[] fieldName = {l.get("Название поля")};
            getUIElement(Input.class, fieldName).sendKeys(l.get("Значение"));
        }
    }

    /**
     * @deprecated предлагаю удалить и не использовать этот и подобные методы, так как они создают дополнительный
     * слой абстракции который сложно понимать и поддерживать
     */
    @И("выполнить действие {string}")
    public void executeMethod(String methodName) {
        executeMethodByName(getSearchContext(), methodName);
    }

    /**
     * @deprecated предлагаю удалить и не использовать этот и подобные методы, так как они создают дополнительный
     * слой абстракции который сложно понимать и поддерживать
     */
    @И("выполнить действие {string}, c аргументами {string}")
    public void executeMethod(String methodName, String arg) {
        executeMethodByName(getSearchContext(), methodName, arg);
    }


    @И("ввести в поле ввода значение {string}")
    public void typeIntoInput(String text) {
        getUIElement(Input.class).sendKeys(DataRandomGenerator.replaceAllGeneratingValues(text));
    }

    @И("ввести в поле ввода дату {date}")
    public void typeDateIntoInput(LocalDate localDate) {
        ((Input) getUIElement(Input.class)).sendKeys(localDate);
    }


    @Тогда("проверить, что в поле значение = {string}")
    public void checkInputValueEquals(String expectedValue) {
        String actualValue = getUIElement(Input.class).getText();
        expectedValue = DataRandomGenerator.replaceAllGeneratingValues(expectedValue);

        softAssert().assertEquals(expectedValue, actualValue, "Текст элемента =" + actualValue
                + "'. Не совпадает с ожидаемым значением: '" + expectedValue + '\'');
    }

    @Тогда("проверить, что в поле поиска значение = {string}")
    public void checkSearchInputValueEquals(String expectedValue) {
        String actualValue = getUIElement(Input.class).getAttribute("value");
        expectedValue = DataRandomGenerator.replaceAllGeneratingValues(expectedValue);

        softAssert().assertEquals(expectedValue, actualValue, "Текст элемента =" + actualValue
                + "'. Не совпадает с ожидаемым значением: '" + expectedValue + '\'');
    }

    @Тогда("проверить, что в  {element} значение = {string}")
    public void checkInputValueEquals(UIElement element, String expectedValue) {
        String actualValue = element.getText();

        softAssert().assertEquals(expectedValue, actualValue, "Текст элемента  = '" + actualValue
                + "'. Не совпадает с ожидаемым значением: '" + expectedValue + '\'');
    }

    @И("ввести в поле {element} значение {string} и проверить введенное значение.")
    public void typeIntoInputAndCheckValue(UIElement element, String expectedValue) {
        typeIntoInput(element, expectedValue);
        checkInputValueEquals(element, expectedValue);
    }

    @И("очистить поле {element}")
    public void clearInput(UIElement element) {
        element.clear();
    }

    @И("активировать чекбокс")
    public void selectCheckbox() {
        CheckBox checkBox = getUIElement(CheckBox.class);
        checkBox.setChecked(true);
    }


    @И("деактивировать чекбокс")
    public void deselectCheckbox() {
        CheckBox checkBox = getUIElement(CheckBox.class);
        checkBox.setChecked(false);
    }


    @Тогда("проверить, что чекбокс активирован")
    public void checkCheckboxSelected() {
        CheckBox checkBox = getUIElement(CheckBox.class);
        boolean isSelected = checkBox.isSelected();
        softAssert().assertTrue(isSelected, "Ожидалось что чекбокс  активирован.");
    }


    @Тогда("чекбокс не активирован")
    public void checkCheckboxNotSelected() {
        CheckBox checkBox = getUIElement(CheckBox.class);
        boolean isSelected = checkBox.isSelected();
        softAssert().assertFalse(isSelected, "Ожидалось что чекбокс  не активирован.");
    }


    @И("выбрать радиокнопку")
    public void selectRadioButton() {
        RadioButton radioButton = getUIElement(RadioButton.class);
        radioButton.select();
    }


    @То("радиокнопка выбрана")
    public void checkRadioButtonSelected() {
        RadioButton radioButton = getUIElement(RadioButton.class);
        boolean isSelected = radioButton.isSelected();
        softAssert().assertTrue(isSelected, "Ожидалось что радиокнопка  выбрана.");
    }


    @То("^радиокнопка не выбрана")
    public void checkRadioButtonNotSelected() {
        RadioButton radioButton = getUIElement(RadioButton.class);
        boolean isSelected = radioButton.isSelected();
        softAssert().assertFalse(isSelected, "Ожидалось что радиокнопка  не выбрана.");
    }

    /**
     * Check the text in Page
     *
     * @param text expected text
     */
    @Тогда("отображается элемент с текстом {string}")
    public void assertElementHasText(String text) {
        getUIElement(Text.class, text).should(displayed(), 5);
    }

    @Тогда("текущий блок содержит текст {string}")
    public void assertBlockHasText(String expectedText) {
        String actualText = getCurrentBlock().getText();
        softAssert().assertTrue(actualText.toLowerCase().contains(expectedText.toLowerCase()), "Текст в блоке '" + actualText + "'. Не содержит текст: '" + expectedText + "'");
    }


    /**
     * Check the link address of the element
     *
     * @param expectedLinkAddress expected link address
     */
    @Тогда("проверить что адрес ссылки  = {string}")
    public void checkLinkAddress(String expectedLinkAddress) {
        String actualLinkAddress = ((Link) getUIElement(Link.class)).getLinkAddress();
        softAssert().assertEquals(expectedLinkAddress, actualLinkAddress, "Адрес ссылки '" + actualLinkAddress + "'. Не совпадает с ожидаемым значением: '" + expectedLinkAddress + "'");
    }

    @И("в выпадающем списке  выбрать значение {string}")
    public void selectInDropdown(String value) {
        ((DropDown) getUIElement(DropDown.class)).selectByValue(value);
    }

    /**
     * Select options in dropdown
     *
     * @param values options
     */
    @И("в выпадающем списке выбрать:")
    public void selectMultipleValuesInDropdown(List<String> values) {
        ((DropDown) getUIElement(DropDown.class)).selectMultipleItemsInDropdown(values.toArray(new String[0]));
    }

    /**
     * Check selected option in dropdown
     *
     * @param expectedValue expected option
     */
    @Тогда("в выпадающем списке  выбрано {string}")
    public void checkThatValueInDropdownChosen(String expectedValue) {
        String actualValue = ((DropDown) getUIElement(DropDown.class)).getSelectedInDropdownValue();
        softAssert().assertEquals(expectedValue, actualValue, "Выбранное в элементе  значение '" + actualValue + "'  не соответствует ожидаемому значению '" + expectedValue + "'");
    }


    @То("элемент {element} отображен")
    public void checkIsDisplayed(UIElement element) {
        softAssert().assertTrue(element.isDisplayed(), "Элемент '" + element.getClass().getInterfaces()[0] + "'  не отображён");
    }

    @То("элемент {element} не отображается")
    public void checkNotDisplayed(UIElement element) {
        softAssert().assertFalse(element.isDisplayed(), "Элемент '" + element.getClass().getInterfaces()[0] + "'  не должен отображаться");
    }


    @То("элемент {element} доступен")
    public void checkIsEnabled(UIElement element) {
        softAssert().assertTrue(element.isEnabled(), "Элемент '" + element.getClass().getInterfaces()[0] + "' заблокирован на странице");
    }


    @То("элемент {element} заблокирован")
    public void isDisabled(UIElement element) {
        softAssert().assertFalse(element.isEnabled(), "Элемент '" + element.getClass().getInterfaces()[0] + "' не заблокирован на странице");
    }

    @И("подождать, когда элемент {element} станет видимым")
    public void waitUntilVisible(UIElement element) {
        element.should(displayed());
    }

    @И("подождать, когда элемент {element} исчезнет")
    public void waitUntilInVisible(UIElement element) {
        element.should(not(displayed()));
    }

    @И("подождать, когда элемент {element} станет доступен")
    public void waitUntilEnabled(UIElement element) {
        element.should(EnabledMatcher.enabled());
    }

    @И("элемент {element} присутствует на странице")
    public void checkElementWithText(UIElement element) {
        softAssert().assertTrue(element.isDisplayed(), "Элемент с текстом '" + element.getClass().getInterfaces()[0] + "'отсутствует");
    }


    @И("нажать на элемент с текстом {string}")
    public void findAndExecute(String elementText) {
        getUIElement(Text.class, elementText).click();
    }

    /**
     * Find element on page by text and execute some action.
     * Available actions : "нажать","проверить на видимость","проверить на отсутствие",
     * "проверить на доступность","проверить на недоступность"
     *
     * @param elementText text the element
     * @param action      action
     */
    @И("^найти (?:элемент) с текстом '(.*)'" +
            " и выполнить действие " +
            "(нажать|проверить на видимость|проверить на отсутствие|проверить на доступность|проверить на недоступность)$")
    public void findAndExecute(String elementText, String action) {
        Text element = getUIElement(Text.class, elementText);
        switch (action) {
            case "нажать": {
                element.click();
                break;
            }
            case "проверить на видимость": {
                element.should(displayed());
                break;
            }
            case "проверить на отсутствие": {
                element.should(not(displayed()));
                break;
            }
            case "проверить на доступность": {
                element.should(EnabledMatcher.enabled());
                break;
            }
            case "проверить на недоступность": {
                element.should(not(EnabledMatcher.enabled()));
                break;
            }
        }
    }


    @И("^переключиться на новое окно > c переходом на страницу '(.*)'$")
    public void switchToNewPage(String pageTitle) {
        switchToWindow(pageTitle);
        changePageTo(pageTitle);
    }

    /**
     * Switch driver focus to window/tab and change current page in PageCatalog
     *
     * @param pageTitle - title window/tab
     * @param pageName  -  page by title{@link ru.lanit.at.pages.annotations.Title
     */
    @И("^переключиться на новое окно '(.*)' > c переходом на страницу '(.*)'$")
    public void switchToWindow(String pageTitle, String pageName) {
        switchToWindow(pageTitle);
        changePageTo(pageName);
    }

    private void switchToWindow(String pageTitle) {
        WebDriver webDriver = getDriver();
        String initialTabId = webDriver.getWindowHandle();
        String newTabId = getNewTabId(pageTitle);
        webDriver.switchTo().window(initialTabId);
        webDriver.close();
        webDriver.switchTo().window(newTabId);
    }

    private String getNewTabId(String pageTitle) {
        WebDriver driver = getDriver();
        Set<String> newTabs = driver.getWindowHandles();
        for (String tab : newTabs) {
            driver.switchTo().window(tab);
            if (driver.getTitle().equalsIgnoreCase(pageTitle)) {
                return tab;
            }
        }
        throw new FrameworkRuntimeException("Не найдена вкладка с именем" + pageTitle);
    }


    @И("нажать {type} {string}")
    public void clickElementByName(Class<? extends AtlasWebElement> type, String name) {
        getElementByName(name, type).click();
    }

    @Тогда("открыта страница {string}")
    public void isPageOpen(String pageTitle) {
        resetCurrentBlock();
        AbstractPage page = getPageByTitle(pageTitle);
        softAssert().assertTrue(page.isOpen(), "Страница '" + pageTitle + "' не открыта");

    }

    @Если("в {type} {string} ввести {string}")
    public void sendKeysToElement(Class<? extends AtlasWebElement> type, String elementName, String args) {
        getElementByName(elementName, type).sendKeys(args);
    }

    // todo "соответствует" переделать в тип "вид сравнения", чтобы можно было использовать
    //  сравнения "содержит, не содержит, не соответствует/не равно" и т.п.
    @Тогда("в поле {string} значение соответствует {string}")
    public void checkElementTextMatch(String elementName, String expectedValue) {
        String actualText = getElementByName(elementName, UIElement.class).getText();
        softAssert().assertEquals(actualText, expectedValue);
    }

    @Если("в {string} ввести {string}")
    public void вВвести(String elementName, String value) {
        getElementByName(elementName, AtlasWebElement.class).sendKeys(value);
    }
}

