package white.goo.constant;

public enum RedisKey {
    User("user:","用户key");

    private String value;
    private String msg;

    RedisKey(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public String getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }
}
