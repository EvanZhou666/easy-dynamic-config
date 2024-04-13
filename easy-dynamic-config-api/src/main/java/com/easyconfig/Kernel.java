package com.easyconfig;

import com.easyconfig.builders.Buildable;

public interface Kernel extends Buildable {

    /**
     * 获取配置属性
     * @param clazz 注意:必须是普通的pojo 不能是接口,抽象类等
     * @return
     * @param <T>
     */
    public <T> T getProps(Class<T> clazz);

}
