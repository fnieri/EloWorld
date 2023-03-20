package Enum;

public enum FriendReqActions {
    FOLLOW_FRIEND,
    REMOVE_FRIEND;

    public String serialized() {
        return switch(this) {
            case FOLLOW_FRIEND -> "send_friend";
            case REMOVE_FRIEND -> "remove_friend";
        };
    }
}
