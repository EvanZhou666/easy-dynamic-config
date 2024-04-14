package com.easyconfig.builders.storage;

import com.easyconfig.builders.kernel.ConfigLoaderBuilder;
import com.easyconfig.storage.MysqlConfigLoader;
import com.easyconfig.store.ConfigLoader;

import javax.sql.DataSource;

public class MysqlConfigLoaderBuilder implements ConfigLoaderBuilder {

    private DataSource dataSource;

    @Override
    public ConfigLoader build() {
        return new MysqlConfigLoader(dataSource);
    }

    @Override
    public ConfigLoaderBuilder withDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

}
