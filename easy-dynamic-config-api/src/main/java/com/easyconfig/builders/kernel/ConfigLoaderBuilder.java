package com.easyconfig.builders.kernel;

import com.easyconfig.builders.ECBuilder;
import com.easyconfig.store.ConfigLoader;

import javax.sql.DataSource;

public interface ConfigLoaderBuilder extends ECBuilder<ConfigLoader> {

    /**
     * config jdbc DataSource
     * @param dataSource
     * @return
     */
    ConfigLoaderBuilder withDataSource(DataSource dataSource);

}
