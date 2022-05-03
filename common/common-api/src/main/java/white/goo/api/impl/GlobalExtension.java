package white.goo.api.impl;

import white.goo.api.Extension;
import white.goo.dto.PointProxyView;

import java.io.Serializable;
import java.util.Map;

/**
 * 全局扩展点映射类
 * @author shiyk
 * @date 2022/5/3
 */
public class GlobalExtension implements Extension,Serializable {

    private final Map<String, String> uriList;
    private Object config;
    private Object provider;
    private String module;
    private String label;
    private String id;
    private final Class<?> baseOn;

    public GlobalExtension(Map<String, String> uriList, Class<?> baseOn) {
        this.uriList = uriList;
        this.baseOn = baseOn;
    }
    public void setModule(String module) {
        this.module = module;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setConfig(Object config) {
        this.config = config;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getModule() {
        return module;
    }

    @Override
    public <T> T getProvider() {
        if(provider == null){
            provider = PointProxyView.newInstance(baseOn, uriList, module);
        }
        return (T) provider;
    }

    @Override
    public <T> T getConfig() {
        return (T) config;
    }
}
