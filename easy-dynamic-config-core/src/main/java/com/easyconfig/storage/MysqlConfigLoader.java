package com.easyconfig.storage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easyconfig.exception.QueryConfigException;
import com.easyconfig.store.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlConfigLoader implements ConfigLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlConfigLoader.class);

    private DataSource dataSource;

    public MysqlConfigLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String load(String id) {
        List<FieldNode> fieldNodes = queryDatabase(id);
        Map<String, Object> jsonMap = resumeJSON(fieldNodes);
        return JSON.toJSONString(jsonMap);
    }

    private Map<String, Object> resumeJSON(List<FieldNode> fieldNodes) {
        Map<String, Object> root = new HashMap<>();
        List<FieldNode> nodes = findChildren(0L, fieldNodes);
        for (FieldNode fieldNode : nodes) {
            Object node = parseFieldValue(fieldNode, fieldNodes);
            root.put(fieldNode.fieldName, node);
        }
        return root;
    }

    private Object parseFieldValue(FieldNode fieldNode, List<FieldNode> fieldNodes) {
        if ("Array<Object>".equals(fieldNode.fieldType)) {
            List<Object> nodes = new ArrayList<>(8);
            List<FieldNode> children = findChildren(fieldNode.fieldId, fieldNodes);
            for (FieldNode child : children) {
                Object node = parseFieldValue(child, fieldNodes);
                nodes.add(node);
            }
            return nodes;
        } else if ("Array".equals(fieldNode.fieldType)) {
            // if we get Array<String>, Deserialize from json string
            return JSONObject.parseArray((String) fieldNode.fieldValue, String.class);
        } else if ("Object".equals(fieldNode.fieldType)) {
            // if we get  Object，Recursion
            Map<String, Object> m = new HashMap<>(8);
            List<FieldNode> children = findChildren(fieldNode.fieldId, fieldNodes);
            for (FieldNode child : children) {
                Object node = parseFieldValue(child, fieldNodes);
                m.put(child.fieldName, node);
            }
            return m;
        } else if ("String".equals(fieldNode.fieldType)) {
            // 如果字段类型是 String，则直接返回字符串值
            return fieldNode.fieldValue.toString();
        } else if ("Integer".equals(fieldNode.fieldType)) {
            // 如果字段类型是 Integer，则将字段值转换为整数
            return Integer.parseInt(fieldNode.fieldValue.toString());
        } else if ("Double".equals(fieldNode.fieldType)) {
            return Double.parseDouble(fieldNode.fieldValue.toString());
        } else if ("Float".equals(fieldNode.fieldType)) {
            return Float.parseFloat(fieldNode.fieldValue.toString());
        } else if ("Boolean".equals(fieldNode.fieldType)) {
            return Boolean.parseBoolean(fieldNode.fieldValue.toString());
        } else if ("Byte".equals(fieldNode.fieldType)) {
            return Byte.parseByte(fieldNode.fieldValue.toString());
        } else if ("Character".equals(fieldNode.fieldType)) {
            return fieldNode.fieldValue;
        } else {
            return fieldNode.fieldValue;
        }

    }

    private List<FieldNode> findChildren(Long fieldId, List<FieldNode> fieldNodes) {
        List<FieldNode> children = new ArrayList<>(8);
        for (FieldNode fieldNode : fieldNodes) {
            if (fieldNode.parentFieldId.equals(fieldId)) {
                children.add(fieldNode);
            }
        }
        return children;
    }

    private List<FieldNode> queryDatabase(String id) {
        List<FieldNode> fieldNodeList = new ArrayList<>(16);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("select id, parent_field, field_name,  field_type  , field_value from ek_dynamic_config where class_name=?");
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long fid = resultSet.getLong("id");
                Long parentField = resultSet.getLong("parent_field");
                String fieldName = resultSet.getString("field_name");
                String fieldType = resultSet.getString("field_type");
                String fieldValue = resultSet.getString("field_value");
                FieldNode node = new FieldNode(fid, parentField, fieldName, fieldType, fieldValue);
                fieldNodeList.add(node);
            }
        } catch (SQLException e) {
            LOGGER.error("", e);
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
        return fieldNodeList;
    }


}
