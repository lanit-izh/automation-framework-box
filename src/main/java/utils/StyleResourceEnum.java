package utils;

public enum StyleResourceEnum {
    MOUSE_TRACKER("MouseTracker.css");

    private final String value;

    StyleResourceEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}