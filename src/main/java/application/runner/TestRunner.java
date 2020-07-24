package application.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.PickleEventWrapper;
import cucumber.api.testng.TestNGCucumberRunner;
import cucumber.runtime.model.CucumberFeature;
import org.openqa.selenium.MutableCapabilities;
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

        glue = {"steps", "hooks"},
        features = "target/test-classes/features"
)
public class TestRunner
        implements ITest {
    TestNGCucumberRunner testNGCucumberRunner = new TestNGCucumberRunner(TestRunner.class);
    private String mTestCaseName = "";

    public void runRequestScenario(PickleEventWrapper pickleWrapper, CucumberFeatureWrapper featureWrapper) throws Throwable {
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
        Method method = testNGCucumberRunner.getClass().getDeclaredMethod("getFeatures");
        method.setAccessible(true);
        List<CucumberFeature> features = (List<CucumberFeature>) method.invoke(testNGCucumberRunner);
        mTestCaseName = features.get(0).getPickles().get(0).pickle.getName();
        runRequestScenario(new PickleEventWrapperImpl(features.get(0).getPickles().get(0)), new CucumberFeatureWrapperImpl(features.get(0)));
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
