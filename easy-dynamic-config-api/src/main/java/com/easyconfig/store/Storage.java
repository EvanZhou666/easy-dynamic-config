package com.easyconfig.store;


/**
 * 配置信息存储
 */
public interface Storage {

    /**
     * 从存储中获取配置
     * @param id
     * @return
     */
    <T> T get(String id);

    <T> void put(String id, T instance);

    <T> void remove(String id);

}
