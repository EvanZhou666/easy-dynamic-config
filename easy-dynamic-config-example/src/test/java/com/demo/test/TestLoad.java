package com.demo.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestLoad {

    public static void main(String[] args) throws IOException {
        Path path = FileSystems.getDefault().getPath(".", "easy-dynamic-config-example", "src", "main", "resources", "11.json");

        System.out.println(path.toAbsolutePath());
        List<String> lines = Files.readAllLines(path);
        String jsonDescription = String.join("", lines);


        // 使用 Fastjson 解析 JSON 描述信息
        JSONArray jsonArray = JSONArray.parseArray(jsonDescription);

        // 创建用于存储还原后的 JSON 数据的 JSONObject
        JSONObject originalJson = new JSONObject();

        // 遍历描述信息中的每个字段，并根据字段名和字段值添加到 originalJson 中
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject fieldObject = jsonArray.getJSONObject(i);
            String fieldName = fieldObject.getString("field");
            Object fieldValue = parseFieldValue(fieldObject.get("fieldType"), fieldObject.get("fieldValue"));
            originalJson.put(fieldName, fieldValue);
        }

        // 将 originalJson 转换为字符串输出
        String originalJsonString = originalJson.toJSONString();
        System.out.println(originalJsonString);
    }

    private static Object parseFieldValue(Object fieldType, Object fieldValue) {
        if ("Array<Object>".equals(fieldType)) {
            // 如果字段类型是 Array<String>，则直接返回字符串数组
            JSONArray jsonArray = (JSONArray) fieldValue;
            JSONArray newArray = new JSONArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Object o = parseFieldValue(jsonObject.getString("fieldType"), jsonObject.getString("fieldValue"));
                JSONObject newField = new JSONObject();
                newField.put(jsonObject.getString("field"), o);
                newArray.add(newField);
            }
            return newArray;
        } else if ("Array".equals(fieldType)) {
            // 如果字段类型是 Array<String>，则直接返回字符串数组
            JSONArray jsonArray = (JSONArray) fieldValue;
            return jsonArray.toArray(new String[0]);
        } else if ("Object".equals(fieldType)) {
            // 如果字段类型是 Object，则递归解析子对象
            JSONArray jsonArray = (JSONArray) fieldValue;
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                map.put(jsonObject.getString("field"), jsonObject.getString("fieldValue"));
            }
            return map;
        } else if ("String".equals(fieldType)) {
            // 如果字段类型是 String，则直接返回字符串值
            return fieldValue.toString();
        } else if ("Integer".equals(fieldType)) {
            // 如果字段类型是 Integer，则将字段值转换为整数
            return Integer.parseInt(fieldValue.toString());
        } else if ("Double".equals(fieldType)) {
            return Double.parseDouble(fieldValue.toString());
        } else if ("Float".equals(fieldType)) {
            return Float.parseFloat(fieldValue.toString());
        } else if ("Boolean".equals(fieldType)) {
            return Boolean.parseBoolean(fieldValue.toString());
        } else if ("Byte".equals(fieldType)) {
            return Byte.parseByte(fieldValue.toString());
        } else if ("Character".equals(fieldType)) {
            return fieldValue;
        } else {
            return fieldValue;
        }
    }
}
