package Enum;

public enum Domain {
    AUTH,
    FRIEND,
    ENTRY;

    public String serialized() {
        return switch (this) {
            case AUTH -> "auth";
            case FRIEND -> "friend";
            case ENTRY -> "entry";
        };
    }
}
