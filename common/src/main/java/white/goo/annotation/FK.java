package white.goo.annotation;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.lang.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface FK {

    /**
     * 对应实体的class
     * @return
     */
    @NonNull
    Class<?> value();

    /**
     * 用于查询实体的service
     * @return
     */
    @NonNull
    Class<? extends IService<?>> service();

}