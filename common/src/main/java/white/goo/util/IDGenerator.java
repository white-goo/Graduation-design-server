package white.goo.util;


import com.baidu.fsg.uid.impl.CachedUidGenerator;

public class IDGenerator {

    public static String generatorID(){
        return String.valueOf(((CachedUidGenerator) SpringUtil.getBean("cachedUidGenerator")).getUID());
    }

}
