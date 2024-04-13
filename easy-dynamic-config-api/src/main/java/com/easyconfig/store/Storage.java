package com.easyconfig.store;


import com.easyconfig.builders.Buildable;

/**
 * 配置信息存储
 */
public interface Storage extends Buildable {

    /**
     * 从存储中获取配置
     * @param id
     * @return
     */
    <T> T get(String id);

    <T> void put(String id, T instance);

    <T> void remove(String id);

}
