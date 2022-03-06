package white.goo.constant;

public enum CourseStatusEnum {

    NOT_START("1","课程尚未开始"),
    START("2", "课程已经开始"),
    END("3","课程已经结束"),
    FULL("4","课程已满")
    ;


    private String value;
    private String message;

    CourseStatusEnum(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getValue(){
        return value;
    }

}
