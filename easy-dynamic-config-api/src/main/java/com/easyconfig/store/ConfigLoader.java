package com.easyconfig.store;

import com.easyconfig.builders.Buildable;

import java.util.Properties;

public interface ConfigLoader extends Buildable {

    /**
     * load config by config tag
     * @param cfTag config tag
     * @return
     */
    Properties load(String cfTag);
}
