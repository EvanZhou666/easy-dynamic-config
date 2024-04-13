package com.easyconfig.builders;

import com.easyconfig.DefaultKernel;
import com.easyconfig.Kernel;
import com.easyconfig.builders.kernel.KernelBuilder;
import com.easyconfig.storage.MemoryStorage;
import com.easyconfig.storage.MysqlConfigLoader;
import com.easyconfig.store.Storage;

public class DefaultKernelBuilder implements KernelBuilder {
    private Storage storage;

    @Override
    public Kernel build() {
        if (storage == null) {
            storage = new MemoryStorage();
        }
        return new DefaultKernel(storage, new MysqlConfigLoader());
    }

    @Override
    public DefaultKernelBuilder withStorage(Storage storage) {
        if (storage == null) {
            this.storage = new MemoryStorage();
        }
        return this;
    }
}
