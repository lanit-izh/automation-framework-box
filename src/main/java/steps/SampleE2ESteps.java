package steps;

import cucumber.api.java.ru.И;
import utils.data.helpers.DataRandomGenerator;


public class SampleE2ESteps extends BaseSteps {
    @И("сохранить ссылку для e2e теста с ключом {string}")
    public void openApp(String key) {
        String url = getCurrentPage().getWrappedDriver().getCurrentUrl();
        DataRandomGenerator.replaceAllGeneratingValues("ДатаПровайдер!(" + key + "," + url + ")");
    }
}
