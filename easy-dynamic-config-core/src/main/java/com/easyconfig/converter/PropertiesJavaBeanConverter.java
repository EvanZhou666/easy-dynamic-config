package com.easyconfig.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author evan zhou
 * @version 1.0.0
 * @createTime 2024/8/1 19:27
 */
public class PropertiesJavaBeanConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesJavaBeanConverter.class);

    private static final Pattern DATE_TIME_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final DateTimeFormatter FORMATTER1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");


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
            throws NoSuchFieldException, IllegalAccessException {
        String[] nestedProperties = propertyName.split("\\.");

        Object currentObject = bean;
        Class<?> currentClass = bean.getClass();

        String nestedPropertyName = nestedProperties[0];

        if (nestedPropertyName.contains("[")) {
            // 处理数组或集合索引
            String basePropertyName = nestedPropertyName.substring(0, nestedPropertyName.indexOf('['));
            String indexStr = nestedPropertyName.substring(nestedPropertyName.indexOf('[') + 1, nestedPropertyName.indexOf(']'));
            int index = Integer.parseInt(indexStr);
            Field field = getField(currentClass, basePropertyName);
            if (field == null) {
                return;
            }
            field.setAccessible(true);

            Object collectionValue = field.get(currentObject);
            if (collectionValue == null) {
                collectionValue = instantiateCollection(field.getType());
                field.set(currentObject, collectionValue);
            }
            Type fieldType = field.getGenericType();
            if (fieldType instanceof ParameterizedType) {
                // 处理泛型类型（如 List<T>）
                ParameterizedType parameterizedType = (ParameterizedType) fieldType;
                Type[] typeArgs = parameterizedType.getActualTypeArguments();
                if (typeArgs.length > 0 && typeArgs[0] instanceof Class) {
                    Class<?> elementType = (Class<?>) typeArgs[0];
                    if (isWrapperPrimitive(elementType) || elementType.isPrimitive()) { // 基本类型数据
                        ((Collection<Object>) collectionValue).add(primitiveOrWrapperPrimitive(elementType, propertyValue));
                    } else if (BigDecimal.class == elementType) {
                        ((Collection<Object>) collectionValue).add(new BigDecimal(propertyValue));
                    } else { // 自定义类型（pojo）
                        Object[] objects = ((Collection<Object>) collectionValue).toArray();
                        while (index > objects.length - 1) { // 因为处理属性名的时候是乱序的，不一定按照p[0].name, p[1].name的顺序处理
                            Object subBean = instantiateBean(elementType, currentObject);
                            ((Collection<Object>) collectionValue).add(subBean);
                            objects = ((Collection<Object>) collectionValue).toArray();
                        }

                        if (hasNextPropertiesName(propertyName)) {
                            setProperty(objects[index], splitNextPropertiesName(propertyName), propertyValue);
                        }

                    }

                }
            }

        } else {
            Field field = getField(currentClass, nestedPropertyName);
            if (field == null) {
                return;
            }
            field.setAccessible(true);
            if (field.getType().isPrimitive() || isWrapperPrimitive(field.getType())) { // 如果是基本类型
                field.set(bean, primitiveOrWrapperPrimitive(field.getType(), propertyValue));
            } else if (field.getType() == BigDecimal.class) {
                field.set(bean, new BigDecimal(propertyValue));
            } else if (isTimeType(field.getType())) {
                try {
                    if (propertyValue != null && !"".equals(propertyValue)) {
                        LocalDateTime localDateTime = parseFromString(propertyValue);
                        field.set(bean, ajust2ProperType(field.getType(), localDateTime));
                    }
                } catch (Exception e) {
                    try {
                        LocalDateTime localDateTime = parseFromTimestamp(Long.parseLong(propertyValue));
                        field.set(bean, ajust2ProperType(field.getType(), localDateTime));
                    } catch (Exception ex) {

                    }

                }

            } else { // 对象类型
                Object nestedBean = null;
                if ((nestedBean = field.get(bean)) == null) {
                    nestedBean = instantiateBean(field.getType(), currentObject);
                }

                if (hasNextPropertiesName(propertyName)) {
                    setProperty(nestedBean, splitNextPropertiesName(propertyName), propertyValue);
                }
                field.set(currentObject, nestedBean);
            }

        }

    }


    private static boolean isTimeType(Class<?> type) {
        return LocalDateTime.class == type || LocalDate.class == type || Date.class == type;
    }

    private static boolean isWrapperPrimitive(Class<?> type) {
        if (Integer.class == type || Long.class == type || String.class == type || Double.class == type
                || Float.class == type || Character.class == type  || Byte.class == type || Boolean.class == type) {
            return true;
        }
        return false;
    }

    /**
     * bean初始化
     * @param clazz
     * @return
     * @param <T>
     */
    private static <T> T instantiateBean(Class<T> clazz, Object outerBean) {
        T bean = null;
        try {
            if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
                bean = clazz.getDeclaredConstructor(outerBean.getClass()).newInstance(outerBean);
            } else {
                bean = clazz.getDeclaredConstructor().newInstance(); // 实例化 Java Bean
            }
            return bean;
        } catch (Exception e) {
            LOGGER.error("instantiateBean error", e);
            return bean;
        }
    }

    /**
     * 类型转换
     * @param type
     * @param value
     * @return
     */
    private static Object primitiveOrWrapperPrimitive(Class<?> type, String value) {
        if (Integer.class == type || int.class == type) {
            return Integer.parseInt(value);
        } else if (Long.class == type || long.class == type) {
            return Long.parseLong(value);
        } else if (Double.class == type || double.class == type) {
            return Double.parseDouble(value);
        } else if (Float.class == type || float.class ==  type) {
            return Float.parseFloat(value);
        } else if (String.class == type) {
            return value;
        } else if (Boolean.class == type || boolean.class == type) {
            return Boolean.parseBoolean(value);
        } else if (Character.class == type || char.class == type) {
            if (value.length() > 0) {
                return Character.valueOf(value.charAt(0));
            }
        }
        return value;
    }

    /**
     * 获取下一级属性名
     * @param propertyName
     */

    private static boolean hasNextPropertiesName(String propertyName) {
        if (propertyName.indexOf(".") != -1) {
            return true;
        }
        return false;
    }



    /**
     * 获取下一级属性名
     * @param propertyName
     */

    private static String splitNextPropertiesName(String propertyName) {
        return propertyName.substring(propertyName.indexOf(".") + 1);
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
                return null;
            }
        }
    }

    public static LocalDateTime parseFromString(String dateTimeString) {
        if (DATE_TIME_PATTERN.matcher(dateTimeString).matches()) {
            // 日期时间格式
            return LocalDateTime.parse(dateTimeString, FORMATTER1);
        } else if (DATE_PATTERN.matcher(dateTimeString).matches()) {
            return LocalDate.parse(dateTimeString, FORMATTER2).atStartOfDay();
        } else {
            return null;
        }
    }

    public static LocalDateTime parseFromTimestamp(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }

    /**
     * 时间类型自动调整
     * @param type
     * @param localDateTime
     * @return
     */
    private static Object ajust2ProperType(Class<?> type, LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        if (LocalDateTime.class == type) {
            return localDateTime;
        } else if (LocalDate.class == type) {
            return localDateTime.toLocalDate();
        } else if (Date.class == type){
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

}
