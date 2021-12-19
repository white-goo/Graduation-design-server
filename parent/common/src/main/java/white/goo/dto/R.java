package white.goo.dto;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 同意返回值对象
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> data = new HashMap<>();

    public R() {
        put("code", 20000);
        put("msg", "success");
        super.put("data", data);
    }

    public static R error() {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.data.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R data(String key, Object value){
        data.put(key, value);
        return this;
    }

    public R data(Object value){
        data.put("data", value);
        return this;
    }
}