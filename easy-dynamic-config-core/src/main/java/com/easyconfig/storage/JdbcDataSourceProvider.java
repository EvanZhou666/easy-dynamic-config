package com.easyconfig.storage;

import com.easyconfig.store.DataSourceProvider;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class JdbcDataSourceProvider implements DataSourceProvider {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "password";

    private DataSource dataSource;

    public JdbcDataSourceProvider() {
        this.dataSource = createDataSource(DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    private DataSource createDataSource(String url, String username, String password) {
        dataSource = new JdbcDataSource();
        return dataSource;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    static class JdbcDataSource implements DataSource {
        private String url;
        private String username;
        private String password;
        @Override
        public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(url, username, password);
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return DriverManager.getConnection(url, username, password);
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {

        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {

        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }
    }

    // Other methods for JDBC operations
}
