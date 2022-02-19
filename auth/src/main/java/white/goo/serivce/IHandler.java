package white.goo.serivce;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface IHandler {
    boolean doValidate(Map<String,Object> requestParameter);
}
