package Enum;

public enum FriendReqActions {
    SEND_REQUEST,
    ACCEPT_REQUEST,
    DENY_REQUEST;

    public String serialized() {
        return switch(this) {
            case SEND_REQUEST -> "send_friend";
            case ACCEPT_REQUEST -> "accept_friend";
            case DENY_REQUEST -> "deny_friend";
        };
    }
}
