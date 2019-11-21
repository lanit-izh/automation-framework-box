package utils;

public enum JavaScriptResourceEnum {
    ADD_MOUSE_TRACKER("AddMouseTracker.js"),
    INJECT_STYLE("InjectStyle.js");

    private final String value;

    JavaScriptResourceEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}