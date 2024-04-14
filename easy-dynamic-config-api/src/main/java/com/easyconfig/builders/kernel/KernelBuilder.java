package com.easyconfig.builders.kernel;

import com.easyconfig.Kernel;
import com.easyconfig.builders.ECBuilder;
import com.easyconfig.store.DataSourceProvider;
import com.easyconfig.store.Storage;

public interface KernelBuilder extends ECBuilder<Kernel> {

    /**
     * config Storage
     * @param storage
     * @return
     */
    KernelBuilder withStorage(Storage storage);

    /**
     * config jdbc DataSource Provider
     * @param dataSourceProvider
     * @return
     */
    KernelBuilder withDataSourceProvider(DataSourceProvider dataSourceProvider);

}
