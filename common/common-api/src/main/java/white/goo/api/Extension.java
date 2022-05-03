package white.goo.api;

/**
 * @author shiyk
 * @date 2022/5/3
 */
public interface Extension {

    /**
     * ID，若扩展点未定义ID，则使用注解所在类类名
     *
     * @return
     */
    String getId();

    /**
     * 中文名
     *
     * @return
     */
    String getLabel();

    /**
     * 模块
     *
     * @return
     */
    String getModule();

    /**
     * BaseOn指定的扩展点实体
     *
     * @return
     */
    <T> T getProvider();

    /**
     * 获取配置
     * @return
     */
    <T> T getConfig();

}
