package application.runner;

import cucumber.api.CucumberOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import ru.lanit.at.context.Context;

import java.lang.reflect.Method;

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
    private static Logger LOGGER = LoggerFactory.getLogger(TestRunner.class);private String mTestCaseName = "";

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
