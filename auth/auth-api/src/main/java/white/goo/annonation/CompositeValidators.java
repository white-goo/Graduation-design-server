package white.goo.annonation;

import white.goo.constant.Operator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CompositeValidators {

    /**
     * 操作
     * @return
     */
    Operator opt() default Operator.AND;

    /**
     * 校验器
     *
     * @return
     */
    AuthValidators[] value() default {};

}
