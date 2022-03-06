package white.goo.annonation;

import white.goo.constant.Operator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多级权限校验器 默认and
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthValidators {

    Operator opt() default Operator.AND;

    AuthValidator[] value() default {};

}
