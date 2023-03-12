package src.Enum;

public enum  AuthActions {
    REGISTER,
    LOGIN,
    CLIENT_OK_AUTH,
    CLIENT_BAD_AUTH;

    public String serialized() {
        return switch (this) {
            case REGISTER -> "register";
            case LOGIN -> "login";
            case CLIENT_OK_AUTH -> "client_ok_auth";
            case CLIENT_BAD_AUTH -> "client_bad_auth";

        };
    }
}

