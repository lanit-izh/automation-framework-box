package application.runner.testng;

import cucumber.api.testng.PickleEventWrapper;
import gherkin.events.PickleEvent;

public class PickleEventWrapperImpl implements PickleEventWrapper {
    private final PickleEvent pickleEvent;

    public PickleEventWrapperImpl(PickleEvent pickleEvent) {
        this.pickleEvent = pickleEvent;
    }

    public PickleEvent getPickleEvent() {
        return this.pickleEvent;
    }

    public String toString() {
        return "\"" + this.pickleEvent.pickle.getName() + "\"";
    }
}
