package Enum;

public enum Domain {
    AUTH,
    FRIEND,
    ENTRY,
    RESOURCE,
    FETCH,
    BLOCKCHAIN;

    public String serialized() {
        return switch (this) {
            case AUTH -> "auth";
            case FRIEND -> "friend";
            case ENTRY -> "entry";
            case RESOURCE -> "resource";
            case FETCH -> "fetch";
            case BLOCKCHAIN -> "blockChain";
        };
    }
}
