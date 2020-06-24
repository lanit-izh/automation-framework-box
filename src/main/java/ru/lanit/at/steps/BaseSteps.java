package ru.lanit.at.steps;


import assertion.AssertsManager;
import assertion.ExtendedAssert;

public abstract class BaseSteps extends AbstractFrameworkSteps {
    protected ExtendedAssert softAssert() {
        return AssertsManager.getAssertsManager().softAssert();
    }

    protected ExtendedAssert criticalAssert() {
        return AssertsManager.getAssertsManager().criticalAssert();
    }
}
