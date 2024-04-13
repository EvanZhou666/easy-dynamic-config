package com.easyconfig.builders.kernel;

import com.easyconfig.Kernel;
import com.easyconfig.builders.ECBuilder;
import com.easyconfig.store.Storage;

public interface KernelBuilder extends ECBuilder<Kernel> {

    KernelBuilder withStorage(Storage storage);

}
