package com.easyconfig;

import com.easyconfig.builders.Buildable;

public interface Kernel extends Buildable {

    /**
     * 获取配置属性
     *
     * @param clazz 注意:必须是普通的pojo 不能是接口,抽象类等
     * @param <T>
     * @return
     */
    <T> T getProps(Class<T> clazz);

    /**
     * 更新属性配置
     * @param clazz
     * @param cfName
     * @return
     * @param <T>
     */
    <T> void updateProperties(Class<T> clazz, String cfName);

}
