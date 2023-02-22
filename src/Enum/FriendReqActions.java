package Enum;

public enum FriendReqActions {
    SEND_REQUEST,
    ACCEPT_REQUEST,
    REMOVE_FRIEND;

    public String serialized() {
        return switch(this) {
            case SEND_REQUEST -> "send_friend";
            case ACCEPT_REQUEST -> "accept_friend";
            case REMOVE_FRIEND -> "remove_friend";
        };
    }
}
