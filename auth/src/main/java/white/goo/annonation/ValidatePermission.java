package white.goo.annonation;

import white.goo.constant.Operator;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatePermission {

    @NotNull
    String[] value();

    Operator operator() default Operator.AND;

}
