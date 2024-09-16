package com.easyconfig;

import com.easyconfig.converter.PropertiesJavaBeanConverter;
import com.easyconfig.store.ConfigLoader;
import com.easyconfig.store.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Override
    public <T> void updateProperties(Class<T> clazz, String cfName) {
        String tagName = clazz.getSimpleName();
        Properties properties = configLoader.load(tagName);
        T javaBean = PropertiesJavaBeanConverter.convertToJavaBean(properties, clazz);
        synchronized (this) {
            storage.put(tagName, javaBean);
        }
    }

}
