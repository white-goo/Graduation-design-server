package white.goo.annotation;

import java.lang.annotation.*;

/**
 * 全局扩展点定义注解
 * @author shiyk
 * @date 2022/5/3
 */
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalExtensionPoint {
    /**
     * 插件名
     */
    String label();

    /**
     * 扩展点注解所在类的接口或基类的约束。
     */
    Class<?> baseOn();

    /**
     * 配置类，接收扩展点注解参数信息的地方。 <br>
     */
    Class<?> config();
}