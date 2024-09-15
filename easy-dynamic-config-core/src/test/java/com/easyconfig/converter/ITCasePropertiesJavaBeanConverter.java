package com.easyconfig.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

/**
 * @author evanzhou
 * @version 1.0.0
 * @createTime 2024/8/1 17:12
 */
public class ITCasePropertiesJavaBeanConverter {

    @Test
    public void testConvertToJavaBeanPrimitive() {
        Properties properties = new Properties();
        properties.setProperty("name", "John Doe");
        properties.setProperty("age", "30");
        properties.setProperty("ageWrapper", "30");
        properties.setProperty("male", "true");
        properties.setProperty("maleWrapper", "true");
        properties.setProperty("c", "A");
        properties.setProperty("cWrapper", "A");
        properties.setProperty("score", "90.5");
        properties.setProperty("scoreWrapper", "90.5");
        properties.setProperty("grade", "92.5");
        properties.setProperty("gradeWrapper", "92.5");
        Person person = PropertiesJavaBeanConverter.convertToJavaBean(properties, Person.class);
        System.out.println(person);

        Assertions.assertEquals(person.getName(), "John Doe");
        Assertions.assertEquals(person.getAge(), 30);
        Assertions.assertEquals(person.getAgeWrapper(), 30);
        Assertions.assertEquals(person.getMaleWrapper(), true);
        Assertions.assertTrue(person.isMale());
        Assertions.assertEquals(person.getC(), 'A');
        Assertions.assertEquals(person.getcWrapper(), 'A');
        Assertions.assertEquals(person.getScore(), 90.5D);
        Assertions.assertEquals(person.getScoreWrapper(), 90.5D);
        Assertions.assertEquals(person.getGrade(), 92.5f);
        Assertions.assertEquals(person.getGradeWrapper(), 92.5F);

    }

    @Test
    public void testConvertToJavaBeanList1() {
        Properties properties = new Properties();
        properties.setProperty("name", "John Doe");
        properties.setProperty("age", "30");
        properties.setProperty("children[0].school.name", "ABC 五中");
        properties.setProperty("children[1].school.name", "DEF 一中");

        Person person = PropertiesJavaBeanConverter.convertToJavaBean(properties, Person.class);
        System.out.println(person.getAge()); // 输出: 30
        Assertions.assertEquals(30, person.getAge());
        Assertions.assertEquals(person.getChildren().get(0).getSchool().getName(), "ABC 五中");
        Assertions.assertEquals(person.getChildren().get(1).getSchool().getName(), "DEF 一中");
    }

    @Test
    public void testConvertToJavaBeanList2() {
        Properties properties = new Properties();
        properties.setProperty("name", "John Doe");
        properties.setProperty("age", "30");
        properties.setProperty("children[0].school.name", "ABC 五中");
        properties.setProperty("children[0].school.level", "AAA");

        Person person = PropertiesJavaBeanConverter.convertToJavaBean(properties, Person.class);
        System.out.println(person.getAge()); // 输出: 30
        Assertions.assertEquals(30, person.getAge());
        Assertions.assertEquals(person.getChildren().get(0).getSchool().getName(), "ABC 五中");
        Assertions.assertEquals(person.getChildren().get(0).getSchool().getLevel(), "AAA");
    }

    @Test
    public void testConvertToJavaBeanList3() {
        Properties properties = new Properties();
        properties.setProperty("children[0].school.name", "ABC 五中");
        properties.setProperty("children[0].school.level", "AAA");
        properties.setProperty("children[1].school.name", "ABC 一中");
        properties.setProperty("children[1].school.level", "AAAA");

        Person person = PropertiesJavaBeanConverter.convertToJavaBean(properties, Person.class);
        Assertions.assertEquals(person.getChildren().get(0).getSchool().getName(), "ABC 五中");
        Assertions.assertEquals(person.getChildren().get(0).getSchool().getLevel(), "AAA");
        Assertions.assertEquals(person.getChildren().get(1).getSchool().getName(), "ABC 一中");
        Assertions.assertEquals(person.getChildren().get(1).getSchool().getLevel(), "AAAA");
    }

    @Test
    public void testConvertToJavaBeanList4() {
        Properties properties = new Properties();
        properties.setProperty("children[0].school.name", "ABC 五中");
        properties.setProperty("children[0].school.level", "AAA");
        properties.setProperty("children[1].school.name", "ABC 一中");
        properties.setProperty("children[1].school.level", "AAAA");
        properties.setProperty("children[2].school.name", "ABC 三中");

        Person person = PropertiesJavaBeanConverter.convertToJavaBean(properties, Person.class);
        Assertions.assertEquals(person.getChildren().get(0).getSchool().getName(), "ABC 五中");
        Assertions.assertEquals(person.getChildren().get(0).getSchool().getLevel(), "AAA");
        Assertions.assertEquals(person.getChildren().get(1).getSchool().getName(), "ABC 一中");
        Assertions.assertEquals(person.getChildren().get(1).getSchool().getLevel(), "AAAA");
        Assertions.assertEquals(person.getChildren().get(2).getSchool().getName(), "ABC 三中");
        Assertions.assertNull(person.getChildren().get(2).getSchool().getLevel());
    }


    @Test
    public void testConvertToJavaBeanList5() {
        Properties properties = new Properties();
        for (int i = 0; i < 32; i++) {
            properties.setProperty("children[" + i + "].school.name", "name" + i);
            properties.setProperty("children[" + i + "].school.level", "level" + i);
        }

        Person person = PropertiesJavaBeanConverter.convertToJavaBean(properties, Person.class);
        List<Child> children = person.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Assertions.assertEquals(person.getChildren().get(i).getSchool().getName(), "name" + i);
            Assertions.assertEquals(person.getChildren().get(i).getSchool().getLevel(), "level" + i);
        }
    }

    @Test
    public void testConvertToJavaBeanWithSetCollection1() {
        Properties properties = new Properties();
        properties.setProperty("books[0]", "语文");
        Person person = PropertiesJavaBeanConverter.convertToJavaBean(properties, Person.class);
        System.out.println(person.getBooks()); // 输出: John Doe
        Assertions.assertTrue(person.getBooks().contains("语文"), "set collection出错");


    }

    @Test
    public void testConvertToJavaBeanWithSetCollection2() {
        Properties properties = new Properties();
        properties.setProperty("books[0]", "语文");
        properties.setProperty("books[1]", "数学");
        Person person = PropertiesJavaBeanConverter.convertToJavaBean(properties, Person.class);
        System.out.println(person.getBooks());
        Assertions.assertTrue(person.getBooks().contains("语文"), "set collection出错");
        Assertions.assertTrue(person.getBooks().contains("数学"), "set collection出错");
    }

}
