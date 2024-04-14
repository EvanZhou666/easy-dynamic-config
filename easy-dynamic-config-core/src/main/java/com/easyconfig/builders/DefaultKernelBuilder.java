package com.easyconfig.builders;

import com.easyconfig.DefaultKernel;
import com.easyconfig.Kernel;
import com.easyconfig.builders.kernel.KernelBuilder;
import com.easyconfig.storage.JdbcDataSourceProvider;
import com.easyconfig.storage.MemoryStorage;
import com.easyconfig.storage.MysqlConfigLoader;
import com.easyconfig.store.ConfigLoader;
import com.easyconfig.store.DataSourceProvider;
import com.easyconfig.store.Storage;
import com.easyconfig.utils.ObjectUtils;

public class DefaultKernelBuilder implements KernelBuilder {
    private Storage storage;

    private ConfigLoader configLoader;

    @Override
    public Kernel build() {
        if (storage == null) {
            storage = new MemoryStorage();
        }
        if (configLoader == null) {
            configLoader = new MysqlConfigLoader(new JdbcDataSourceProvider().getDataSource());
        }

        return new DefaultKernel(storage, configLoader);
    }

    @Override
    public DefaultKernelBuilder withStorage(Storage storage) {
        if (storage == null) {
            this.storage = new MemoryStorage();
        }
        return this;
    }

    @Override
    public KernelBuilder withDataSourceProvider(DataSourceProvider dataSourceProvider) {
        ObjectUtils.checkNotNull(dataSourceProvider, "dataSourceProvider must not be null");
        this.configLoader = new MysqlConfigLoader(dataSourceProvider.getDataSource());
        return this;
    }

}
