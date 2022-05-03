package white.goo.extension;

import white.goo.annotation.GlobalExtensionPoint;
import white.goo.api.TestApi;
import white.goo.extension.config.TestConfig;

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
@GlobalExtensionPoint(label = "测试扩展点", baseOn = TestApi.class, config = TestConfig.class)
public @interface TestPoint {

    String code();

}
