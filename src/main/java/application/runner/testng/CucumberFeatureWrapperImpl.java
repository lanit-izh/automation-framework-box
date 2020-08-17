package application.runner.testng;

import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.runtime.model.CucumberFeature;

public class CucumberFeatureWrapperImpl implements CucumberFeatureWrapper {
    private final CucumberFeature cucumberFeature;

    public CucumberFeatureWrapperImpl(CucumberFeature cucumberFeature) {
        this.cucumberFeature = cucumberFeature;
    }

    public String toString() {
        return "\"" + this.cucumberFeature.getGherkinFeature().getFeature().getName() + "\"";
    }
}
