package com.easyconfig.storage;

import com.easyconfig.store.ConfigLoader;

public class MysqlConfigLoader implements ConfigLoader {
    @Override
    public String load(String id) {
        return "{'name':'Evan'}";
    }

}
