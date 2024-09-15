package com.easyconfig;

import com.easyconfig.converter.PropertiesJavaBeanConverter;
import com.easyconfig.store.ConfigLoader;
import com.easyconfig.store.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Properties;

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
        String tagName = clazz.getSimpleName();
        T t = storage.<T>get(tagName);
        if (t != null) {
            return t;
        }
        synchronized (this) {
            t = storage.get(tagName);
            if (t != null) {
                return t;
            }

            Properties properties = configLoader.load(tagName);
            T javaBean = PropertiesJavaBeanConverter.convertToJavaBean(properties, clazz);
            storage.put(tagName, javaBean);
        }
        return storage.get(tagName);
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
