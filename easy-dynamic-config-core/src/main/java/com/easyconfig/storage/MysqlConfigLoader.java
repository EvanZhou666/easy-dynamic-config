package com.easyconfig.storage;

import com.easyconfig.exception.QueryConfigException;
import com.easyconfig.store.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MysqlConfigLoader implements ConfigLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlConfigLoader.class);

    private DataSource dataSource;

    public MysqlConfigLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Properties load(String id) {
        List<ConfigItem> configItems = queryDatabase(id);
        return convertToProperties(configItems);
    }

    private Properties convertToProperties(List<ConfigItem> configItems) {
        Properties properties = new Properties();
        for (ConfigItem configItem : configItems) {
            properties.put(configItem.cfName, configItem.cfValue);
        }
        return properties;
    }

    private List<ConfigItem> queryDatabase(String cfTag) {
        List<ConfigItem> configItemList = new ArrayList<>(16);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("select id, cf_tag, cf_name, cf_value, cf_description, cf_create_time, cf_update_time from ek_dynamic_config where cf_tag=?");
            preparedStatement.setString(1, cfTag);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String cf_tag = resultSet.getString("cf_tag");
                String cf_name = resultSet.getString("cf_name");
                String cf_value = resultSet.getString("cf_value");
                String cf_description = resultSet.getString("cf_description");
                Timestamp createTimestamp = resultSet.getTimestamp("cf_create_time");
                Timestamp updateTimestamp = resultSet.getTimestamp("cf_update_time");
                ConfigItem node = new ConfigItem(id, cf_tag, cf_name, cf_value, cf_description,
                        createTimestamp !=null ?createTimestamp.toLocalDateTime() : null,
                        updateTimestamp !=null ?updateTimestamp.toLocalDateTime() : null);
                configItemList.add(node);
            }
        } catch (SQLException e) {
            LOGGER.error("", e);
            throw new QueryConfigException("datasource configuration incorrectly", e);
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
        return configItemList;
    }


}
