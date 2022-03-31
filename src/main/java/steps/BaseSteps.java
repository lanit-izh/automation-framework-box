package steps;


import assertion.AssertsManager;
import assertion.ExtendedAssert;
import ru.lanit.at.steps.AbstractFrameworkSteps;

public abstract class BaseSteps extends AbstractFrameworkSteps {
    protected ExtendedAssert softAssert() {
        return AssertsManager.getAssertsManager().softAssert();
    }

    protected ExtendedAssert criticalAssert() {
        return AssertsManager.getAssertsManager().criticalAssert();
    }
}
