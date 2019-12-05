package hooks;

import com.consol.citrus.report.MessageTracingTestListener;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import extensions.ElementReadyClickSendKeysExtension;
import extensions.IsDisplayedExtension;
import io.qameta.atlas.core.Atlas;
import steps.BaseSteps;
import utils.AllureHelper;

import java.nio.charset.StandardCharsets;


public class Hooks extends BaseSteps {


    @Before
    public void setUp() {
        Atlas atlas = getAtlas();
        atlas.extension(new ElementReadyClickSendKeysExtension());
        atlas.extension(new IsDisplayedExtension());
    }

    @After
    public void tearDown(Scenario scenario) {
        log.info("Finish scenario " + scenario.getName());
        String message = "Finish scenario";
        AllureHelper.attachTxt("Txt", message);
        AllureHelper.attachPageSource(getDriver().getPageSource().getBytes(StandardCharsets.UTF_8));
        if (driverIsActive()) {
            AllureHelper.attachScreenShot("Скриншот последней операции", getScreenShooter().takeScreenshot());
            shutdownDriver();
        }
        MessageTracingTestListener messageTracingTestListener = (MessageTracingTestListener) getEndpointByName("messageTracingTestListener");
        messageTracingTestListener.onTestFinish(getCitrusRunner().getTestCase());
        assertsManager.softAssert().assertAll();
        assertsManager.softAssert().flush();
    }
}