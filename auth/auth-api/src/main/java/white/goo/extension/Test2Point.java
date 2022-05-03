package white.goo.extension;

import white.goo.annotation.GlobalExtensionPoint;
import white.goo.api.Test2Api;
import white.goo.extension.config.Test2Config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shiyk
 * @date 2022/5/3
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@GlobalExtensionPoint(label = "测试2扩展点", baseOn = Test2Api.class, config = Test2Config.class)
public @interface Test2Point {

    String code();

}
