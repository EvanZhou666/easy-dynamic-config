package com.easyconfig.storage;

import com.easyconfig.exception.QueryConfigException;
import com.easyconfig.store.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlConfigLoader implements ConfigLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlConfigLoader.class);

    private DataSource dataSource;

    public MysqlConfigLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String load(String id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("");
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new QueryConfigException("check, datasource configuration incorrectly", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new QueryConfigException("close resource error", e);
            }
        }
        return "{'name':'Evan'}";
    }

}
