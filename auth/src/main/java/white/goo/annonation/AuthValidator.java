package white.goo.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthValidator {

    /**
     * 校验器id
     * @return
     */
    String value() default "";

    /**
     * 校验器参数
     * @return
     */
    ValidateParam[] param() default {};

}
