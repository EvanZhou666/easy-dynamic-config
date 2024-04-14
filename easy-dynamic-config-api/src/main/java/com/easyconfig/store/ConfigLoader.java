package com.easyconfig.store;

import com.easyconfig.builders.Buildable;

public interface ConfigLoader extends Buildable {

    /**
     * load config by unique id
     * @param id
     * @return
     */
    String load(String id);
}
