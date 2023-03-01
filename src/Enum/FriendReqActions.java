package Enum;

public enum FriendReqActions {
    FOLLOW_FRIEND;

    public String serialized() {
        return switch(this) {
            case FOLLOW_FRIEND -> "send_friend";

        };
    }
}
