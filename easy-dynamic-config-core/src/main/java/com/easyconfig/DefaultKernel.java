package com.easyconfig;

import com.alibaba.fastjson.JSONObject;
import com.easyconfig.store.ConfigLoader;
import com.easyconfig.store.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class DefaultKernel implements Kernel {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKernel.class);

    private Storage storage;
    private ConfigLoader configLoader;

    public DefaultKernel(Storage storage, ConfigLoader configLoader) {
        this.storage = storage;
        this.configLoader = configLoader;
    }

    @Override
    public <T> T getProps(Class<T> clazz) {
        String id = clazz.getSimpleName();
        T t = storage.<T>get(id);
        if (t != null) {
            return t;
        }
        synchronized (this) {
            t = storage.get(id);
            if (t != null) {
                return t;
            }

            // load config
            String load = configLoader.load(id);
            if (load == null || "".equals(load)) {
                try {
                    T instance = clazz.newInstance();
                    storage.put(id, instance);
                } catch (InstantiationException | IllegalAccessException e) {
                    // skip...
                }
            } else {
                T object = JSONObject.parseObject(load, clazz);
                if (object != null) {
                    storage.put(id, object);
                } else {
                    // put Empty Object
                    try {
                        storage.put(id, clazz.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        // skip...
                    }
                }
            }
        }
        return storage.get(id);
    }

    /**
     * 更新配置属性
     *
     * @param <T>
     * @return
     */
    protected synchronized void updateProps(Object newObj) {
        // 不能設置空對象
        if (newObj == null) {
            return;
        }
        // 刪除重建
        // storage.remove(newObj.getClass().getSimpleName());
        Object old = this.getProps(newObj.getClass());
        try {
            updateObject(old, newObj);
        } catch (IllegalAccessException e) {
            LOGGER.error("update object field failure {}", e);
        }
    }

    public static void updateObject(Object oldObject, Object newObject) throws IllegalAccessException {
        Class<?> oldClass = oldObject.getClass();
        Class<?> newClass = newObject.getClass();

        Field[] fields = oldClass.getDeclaredFields();
        for (Field field : fields) {
            // 设置字段为可访问，以便访问私有字段
            field.setAccessible(true);
            // 获取 newObject 中对应字段的值
            Field newField;
            try {
                newField = newClass.getDeclaredField(field.getName());
            } catch (NoSuchFieldException e) {
                // 如果 newObject 中没有对应的字段，则跳过
                continue;
            }
            // 设置 newField 为可访问
            newField.setAccessible(true);
            // 获取 newObject 中字段的值，并设置到 oldObject 中
            Object value = newField.get(newObject);
            field.set(oldObject, value);
        }
    }

}
