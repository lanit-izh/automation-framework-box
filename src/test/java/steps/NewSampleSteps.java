package steps;

import cucumber.api.java.ru.Пусть;
import ru.lanit.at.Config;
import ru.lanit.at.exceptions.FrameworkRuntimeException;


public class NewSampleSteps extends BaseSteps {

    @Пусть("^открываем тестируемое приложение$")
    public void openApp() {
        String url = Config.loadProperty("site.url");
        if (url == null || url.isEmpty())
            throw new FrameworkRuntimeException("Не указан стенд, установите урл для стенда выполнения");
        getDriver().get(url);
    }


}
