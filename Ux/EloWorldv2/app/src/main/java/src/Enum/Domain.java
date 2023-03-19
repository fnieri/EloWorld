package src.Enum;

public enum Domain {
    AUTH,
    FRIEND,
    ENTRY,
    CHECK_ENTRY,
    FETCH,
    BLOCKCHAIN,
    RESOURCE;

    public String serialized() {
        return switch (this) {
            case AUTH -> "auth";
            case FRIEND -> "friend";
            case ENTRY -> "entry";
            case CHECK_ENTRY -> "check_entry";
            case RESOURCE -> "resource";
            case FETCH -> "fetch";
            case BLOCKCHAIN -> "blockchain";
        };
    }
}
