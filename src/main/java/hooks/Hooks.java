package hooks;

import com.consol.citrus.report.MessageTracingTestListener;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import extensions.ElementReadyClickSendKeysExtension;
import extensions.IsDisplayedExtension;
import io.qameta.atlas.core.Atlas;
import ru.lanit.at.context.Context;
import steps.BaseSteps;
import utils.allure.AllureHelper;
import utils.data.helpers.DataProviderHelper;

import java.nio.charset.StandardCharsets;
import java.util.Map;


public class Hooks extends BaseSteps {


    @Before
    public void setUp() {
        Atlas atlas = getAtlas();
        atlas.extension(new ElementReadyClickSendKeysExtension());
        atlas.extension(new IsDisplayedExtension());
        getDpData();
    }

    /**
     * Получение данных из таблицы aliases, где колонка alias_name = alias_input
     */
    private void getDpData() {
        String alias_input = System.getProperty("alias_input");
        if (alias_input != null) {
            if (!alias_input.isEmpty()) {
                log.info("Получение данные по алиасу '" + alias_input + "'.");
                Map<String, Object> dataKeeper = (Map<String, Object>) Context.getInstance().getBean("dataKeeper");
                dataKeeper.putAll(DataProviderHelper.getDpData(alias_input));
            }
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        saveDpData();
        log.info("Finish scenario " + scenario.getName());
        String message = "Finish scenario";
        AllureHelper.attachTxt("Txt", message);
        if (driverIsActive()) {
            try {
                AllureHelper.attachPageSource(getDriver().getPageSource().getBytes(StandardCharsets.UTF_8));
                AllureHelper.attachScreenShot("Скриншот последней операции", getScreenShooter().takeScreenshot());
            } catch (Exception ignore) {
            }
            shutdownDriver();
        }
        MessageTracingTestListener messageTracingTestListener = (MessageTracingTestListener) getEndpointByName("messageTracingTestListener");
        messageTracingTestListener.onTestFinish(getCitrusRunner().getTestCase());
        softAssert().assertAll();
        softAssert().flush();
    }

    /**
     * Сохранение данных в таблицу aliases, где колонка alias_name = alias_output
     */
    private void saveDpData() {
        String alias_output = System.getProperty("alias_output");
        if (alias_output != null) {
            if (!alias_output.isEmpty()) {
                Map<String, Object> data = (Map<String, Object>) Context.getInstance().getBean("dataKeeper");
                log.info("Сохранения по алиасу '" + alias_output + "' данных");
                DataProviderHelper.saveDpData(alias_output, data);
            }
        }
    }
}