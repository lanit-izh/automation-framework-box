package steps;


import assertion.AssertsManager;
import ru.lanit.at.steps.AbstractFrameworkSteps;

public abstract class BaseSteps extends AbstractFrameworkSteps {
    protected AssertsManager assertsManager = AssertsManager.getAssertsManager();
}
