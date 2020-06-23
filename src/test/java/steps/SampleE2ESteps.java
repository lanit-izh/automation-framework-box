package steps;

import cucumber.api.java.ru.И;
import utils.DataGenerator;

public class SampleE2ESteps extends BaseSteps {
    @И("сохранить ссылку для e2e теста с ключом {string}")
    public void openApp(String key) {
        String url = getCurrentPage().getWrappedDriver().getCurrentUrl();
        DataGenerator.replaceAllGeneratingValues("ДатаПровайдер!(" + key + "," + url + ")");
    }
}
