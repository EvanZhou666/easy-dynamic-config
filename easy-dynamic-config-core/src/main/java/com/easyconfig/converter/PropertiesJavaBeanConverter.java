package com.easyconfig.converter;

import java.lang.reflect.*;
import java.util.Properties;
import java.util.*;
/**
 * @author evan zhou
 * @version 1.0.0
 * @createTime 2024/8/1 19:27
 */
public class PropertiesJavaBeanConverter {

    /**
     * 将属性转换为 Java Bean
     *
     * @param properties 要转换的属性
     * @param clazz      Java Bean 的类
     * @param <T>        Java Bean 的类型
     * @return 使用属性值填充后的 Java Bean 实例
     */
    public static <T> T convertToJavaBean(Properties properties, Class<T> clazz) {
        try {
            T bean = clazz.getDeclaredConstructor().newInstance(); // 实例化 Java Bean

            for (String propertyName : properties.stringPropertyNames()) {
                String propertyValue = properties.getProperty(propertyName);
                setProperty(bean, propertyName, propertyValue);
            }

            return bean;
        } catch (Exception e) {
            e.printStackTrace(); // 实际应用中应正确处理异常
            return null;
        }
    }

    /**
     * 设置 Java Bean 中的属性值，支持带有数组的嵌套属性。
     *
     * @param bean           Java Bean 实例
     * @param propertyName   属性名称（支持带点和数组的嵌套属性）
     * @param propertyValue  要设置的属性值
     * @param <T>            Java Bean 的类型
     * @throws NoSuchFieldException 如果找不到指定名称的字段
     * @throws IllegalAccessException 如果字段不可访问
     */
    private static <T> void setProperty(T bean, String propertyName, String propertyValue)
            throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        String[] nestedProperties = propertyName.split("\\.");

        Object currentObject = bean;
        Class<?> currentClass = bean.getClass();

        for (int i = 0; i < nestedProperties.length; i++) {
            String nestedPropertyName = nestedProperties[i];

            if (nestedPropertyName.contains("[")) {
                // 处理数组或集合索引
                String basePropertyName = nestedPropertyName.substring(0, nestedPropertyName.indexOf('['));
                String indexStr = nestedPropertyName.substring(nestedPropertyName.indexOf('[') + 1, nestedPropertyName.indexOf(']'));
                int index = Integer.parseInt(indexStr);

                Field field = getField(currentClass, basePropertyName);
                field.setAccessible(true);

                Object fieldValue = field.get(currentObject);
                if (fieldValue == null) {
                    fieldValue = instantiateCollection(field.getType());
                    Type fieldType = field.getGenericType();
                    if (fieldType instanceof ParameterizedType) {
                        // 处理泛型类型（如 List<T>）
                        ParameterizedType parameterizedType = (ParameterizedType) fieldType;
                        Type[] typeArgs = parameterizedType.getActualTypeArguments();
                        if (typeArgs.length > 0 && typeArgs[0] instanceof Class) {
                            Class<?> elementType = (Class<?>) typeArgs[0];
//                            fieldValue = instantiateCollection(field.getType());
                            field.set(currentObject, fieldValue);
                        }
                    }
                    if (field.getType().isArray()) {
                        // 处理数组类型
                        Class<?> componentType = field.getType().getComponentType();
                        fieldValue = Array.newInstance(componentType, index + 1);
                        field.set(currentObject, fieldValue);
                    } else {
                        throw new IllegalArgumentException("不支持的类型: " + field.getType().getName());
                    }
                }

                currentObject = getArrayElementOrCollectionElement(fieldValue, index);
                currentClass = currentObject.getClass();
            } else {
                // 处理非数组属性
                Field field = getField(currentClass, nestedPropertyName);
                field.setAccessible(true);

                // 如果是最后一级，则设置值
                if (i == nestedProperties.length - 1) {
                    Class<?> fieldType = field.getType();
                    Object value = convertToType(propertyValue, fieldType);
                    field.set(currentObject, value);
                } else {
                    // 如果不是最后一级，则获取或创建嵌套对象
                    Object nestedObject = field.get(currentObject);
                    if (nestedObject == null) {
                        nestedObject = field.getType().getDeclaredConstructor().newInstance();
                        field.set(currentObject, nestedObject);
                    }
                    currentObject = nestedObject;
                    currentClass = currentObject.getClass();
                }
            }
        }
    }

    /**
     * 实例化指定元素类型的集合。
     *
     * @param elementType 集合中元素的类型
     * @return 集合的实例
     * @throws IllegalAccessException 如果无法实例化集合
     * @throws InstantiationException 如果无法实例化集合
     */
    private static Collection<?> instantiateCollection(Class<?> elementType) {
        if (List.class.isAssignableFrom(elementType)) {
            return new ArrayList<>();
        } else if (Set.class.isAssignableFrom(elementType)) {
            return new HashSet<>();
        } else {
            throw new IllegalArgumentException("不支持的集合类型: " + elementType.getName());
        }
    }

    /**
     * 从数组或集合中获取指定索引的元素。
     *
     * @param fieldValue 数组或集合实例
     * @param index      要检索的元素索引
     * @return 指定索引的元素
     */
    private static Object getArrayElementOrCollectionElement(Object fieldValue, int index) {
        if (fieldValue.getClass().isArray()) {
            return Array.get(fieldValue, index);
        } else if (fieldValue instanceof List<?>) {
            List<?> list = (List<?>) fieldValue;
            while (list.size() <= index) {
                list.add(null);
            }
            return list.get(index);
        } else if (fieldValue instanceof Set<?>) {
            Set<?> set = (Set<?>) fieldValue;
            if (set.size() <= index) {
                for (int i = set.size(); i <= index; i++) {
                    set.add(null);
                }
            }
            return set.stream().skip(index).findFirst().orElse(null);
        } else {
            throw new IllegalArgumentException("不支持的集合类型: " + fieldValue.getClass().getName());
        }
    }

    /**
     * 将字符串值转换为指定类型。
     *
     * @param value 要转换的字符串值
     * @param type  要转换为的目标类型
     * @return 转换后的值
     */
    private static Object convertToType(String value, Class<?> type) {
        if (type == String.class) {
            return value;
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        } else if (type == Long.class || type == long.class) {
            return Long.parseLong(value);
        } else if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(value);
        } else {
            throw new IllegalArgumentException("不支持的类型: " + type.getName());
        }
    }

    /**
     * 从类或其任何超类中获取字段。
     *
     * @param clazz        要搜索字段的类
     * @param propertyName 字段的名称
     * @return 字段对象
     * @throws NoSuchFieldException 如果找不到指定名称的字段
     */
    private static Field getField(Class<?> clazz, String propertyName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(propertyName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getField(superClass, propertyName);
            } else {
                throw e;
            }
        }
    }

}
