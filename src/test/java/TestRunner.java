import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.PickleEventWrapper;
import cucumber.api.testng.TestNGCucumberRunner;
import org.testng.annotations.DataProvider;

/** Тест раннер для запуска тестов */
@CucumberOptions(
        plugin = {"pretty",
                "io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm",
                "json:target/cucumber.json",
        },
        glue = {"steps", "hooks"},

        features = "classpath:features"
)
public class TestRunner extends AbstractTestNGCucumberTests {
    private TestNGCucumberRunner testNGCucumberRunner;

    @Override
    public void runScenario(PickleEventWrapper pickleWrapper, CucumberFeatureWrapper featureWrapper) throws Throwable {
        super.runScenario(pickleWrapper, featureWrapper);
    }

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        System.out.println("<<<<<Scenarios count:" + super.scenarios().length);
        return super.scenarios();
    }
}

