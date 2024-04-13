package com.easyconfig.store;

public interface ConfigLoader {

    /**
     * 加载配置
     * @param id
     * @return
     */
    String load(String id);
}
