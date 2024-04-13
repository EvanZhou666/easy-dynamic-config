package com.easyconfig.store;

import javax.sql.DataSource;

public interface DataSourceProvider {
    DataSource getDataSource();
}
