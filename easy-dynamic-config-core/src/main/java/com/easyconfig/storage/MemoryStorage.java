package com.easyconfig.storage;

import com.easyconfig.store.Storage;

import java.util.HashMap;
import java.util.Map;

public class MemoryStorage implements Storage {

    private final Map<String, Object> mapCache = new HashMap<>(16);

    @Override
    public <T> T get(String id) {
        return (T) mapCache.get(id);
    }

    @Override
    public <T> void put(String id, T instance) {
        mapCache.put(id, instance);
    }

    @Override
    public <T> void remove(String id) {
        mapCache.remove(id);
    }

}
