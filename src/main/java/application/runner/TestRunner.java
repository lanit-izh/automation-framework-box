package application.runner;

import application.runner.testng.CucumberTestNgRunner;
import application.runner.testng.PickleEventWrapperImpl;
import cucumber.api.CucumberOptions;
import cucumber.api.testng.PickleEventWrapper;
import cucumber.runtime.model.CucumberFeature;
import org.openqa.selenium.MutableCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import ru.lanit.at.context.Context;
import ru.lanit.at.driver.DriverManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Тест раннер для запуска тестов в режиме веб приложения */
@CucumberOptions(
        plugin = {"pretty",
                "io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm",
                "json:target/cucumber.json",
        },

        glue = {"steps", "hooks"}
)
public class TestRunner
        implements ITest {
    private static Logger LOGGER = LoggerFactory.getLogger(TestRunner.class);
    private CucumberTestNgRunner testNGCucumberRunner;
    private String mTestCaseName = "";


    public TestRunner(String featurePath) {
        testNGCucumberRunner = new CucumberTestNgRunner(TestRunner.class, featurePath);
    }

    public void runRequestScenario(PickleEventWrapper pickleWrapper) throws Throwable {
        try {
            Map<String, String> capabilitiesMap = new HashMap<>();
            capabilitiesMap.put("name", getTestName());
            capabilitiesMap.put("timeZone", "Europe/Moscow");
            Context.getInstance().getBean(DriverManager.class).addCapabilities(new MutableCapabilities(capabilitiesMap));
            testNGCucumberRunner.runScenario(pickleWrapper.getPickleEvent());
        } finally {
            testNGCucumberRunner.finish();
        }
    }

    public void runScenario() throws Throwable {
        List<CucumberFeature> features = testNGCucumberRunner.getFeatures();
        mTestCaseName = features.get(0).getPickles().get(0).pickle.getName();
        CucumberFeature cucumberFeature = features.get(0);
        runRequestScenario(new PickleEventWrapperImpl(cucumberFeature.getPickles().get(0)));
    }

    @BeforeMethod
    public void before(Method method, Object[] testData) {
        Context.removeInstance();
        this.mTestCaseName = testData[0].toString();
    }

    @Override
    public String getTestName() {
        return this.mTestCaseName;
    }
}
