package white.goo.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import white.goo.util.ThreadLocalUtil;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value =Throwable.class)
    public String exceptionHandler(Exception e) throws Exception {
        ThreadLocalUtil.remove();
        throw e;
    }

}
