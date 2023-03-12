package src.Enum;

public enum Domain {
    AUTH,
    FRIEND,
    ENTRY,
    FETCH,
    BLOCKCHAIN,
    RESOURCE;

    public String serialized() {
        return switch (this) {
            case AUTH -> "auth";
            case FRIEND -> "friend";
            case ENTRY -> "entry";
            case RESOURCE -> "resource";
            case FETCH -> "fetch";
            case BLOCKCHAIN -> "blockchain";
        };
    }
}
