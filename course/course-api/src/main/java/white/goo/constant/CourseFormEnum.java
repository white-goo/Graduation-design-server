package white.goo.constant;

public enum CourseFormEnum {
    ONLINE("1", "网课"),
    OFFLINE("2", "线下")
    ;

    private String value;
    private String message;

    CourseFormEnum(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
