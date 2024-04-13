package com.easyconfig.builders.storage;

import com.easyconfig.builders.ECBuilder;
import com.easyconfig.store.Storage;

public interface StorageBuilder extends ECBuilder<Storage> {
    StorageBuilder withUrl(String url);

    StorageBuilder withPort(Integer port);

    StorageBuilder withUsername(String username);
    StorageBuilder withPassword(String password);


}
