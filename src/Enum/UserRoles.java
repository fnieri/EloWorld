package Enum;

public enum UserRoles {
    USER,
    REFEREE,
    SUPER_USER;

    public String serialized() {
        return switch (this) {
            case USER -> "user";
            case REFEREE -> "referee";
            case SUPER_USER -> "super_user";
        };
    }

}
