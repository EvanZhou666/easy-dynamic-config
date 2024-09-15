package com.easyconfig.converter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author evanzhou
 * @version 1.0.0
 * @createTime 2024/8/1 20:13
 */
public class Person {
    private String name;
    private int age;
    private List<Child> children;

    // Getters and setters (或使用 lombok 的注解简化)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }
}
