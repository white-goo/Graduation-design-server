package white.goo.util;

public class ThreadLocalUtil {

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static String get(){
        return threadLocal.get();
    }

    public static void set(String date){
        threadLocal.set(date);
    }

    public static void remove(){
        threadLocal.remove();
    }

}
