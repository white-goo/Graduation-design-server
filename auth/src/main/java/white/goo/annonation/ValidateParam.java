package white.goo.annonation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateParam {

    /**
     * 参数名
     *
     * @return
     */
    String name() default "";

    /**
     * 参数值信息
     *
     * @return
     */
    String[] value();

}
