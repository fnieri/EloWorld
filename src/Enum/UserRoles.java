package Enum;

public enum UserRoles {
    NOT_LOGGED_IN,
    USER,
    REFEREE,
    SUPER_USER;

    public String serialized() {
        return switch (this) {
            case NOT_LOGGED_IN -> "no_role";
            case USER -> "user";
            case REFEREE -> "referee";
            case SUPER_USER -> "super_user";
        };
    }

}
