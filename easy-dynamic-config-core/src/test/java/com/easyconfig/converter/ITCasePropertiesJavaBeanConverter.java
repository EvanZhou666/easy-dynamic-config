package com.easyconfig.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

/**
 * @author evanzhou
 * @version 1.0.0
 * @createTime 2024/8/1 17:12
 */
public class ITCasePropertiesJavaBeanConverter {
    @Test
    public void  testConvertToJavaBean() {
        Properties properties = new Properties();
//        properties.setProperty("name", "John Doe");
//        properties.setProperty("age", "30");
        properties.setProperty("children[0].school.name", "ABC 学校");

        Person person = PropertiesJavaBeanConverter.convertToJavaBean(properties, Person.class);
//        System.out.println(person.getName()); // 输出: John Doe
//        Assertions.assertEquals("John Doe", person.getName());
//        System.out.println(person.getAge()); // 输出: 30
//        Assertions.assertEquals(30, person.getAge());
        System.out.println(person.getChildren().get(0).getSchool().getName());
    }

}
