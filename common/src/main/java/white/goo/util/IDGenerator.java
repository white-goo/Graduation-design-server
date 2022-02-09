package white.goo.util;


import com.baidu.fsg.uid.impl.CachedUidGenerator;

public class IDGenerator {

    public static long generatorID(){
        return ((CachedUidGenerator)SpringUtil.getBean("cachedUidGenerator")).getUID();
    }

}
