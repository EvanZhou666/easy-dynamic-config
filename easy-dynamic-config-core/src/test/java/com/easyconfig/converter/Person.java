package com.easyconfig.converter;

import java.util.List;
import java.util.Set;

/**
 * @author evanzhou
 * @version 1.0.0
 * @createTime 2024/8/1 20:13
 */
public class Person {
    private String name;
    private int age;

    private Integer ageWrapper;

    private boolean male;

    private Boolean maleWrapper;

    private char c;

    private Character cWrapper;

    private double score;

    private Double scoreWrapper;

    private float grade;

    private Float gradeWrapper;

    private List<Child> children;

    private Set<String> books;

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

    public Integer getAgeWrapper() {
        return ageWrapper;
    }

    public void setAgeWrapper(Integer ageWrapper) {
        this.ageWrapper = ageWrapper;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public Boolean getMaleWrapper() {
        return maleWrapper;
    }

    public void setMaleWrapper(Boolean maleWrapper) {
        this.maleWrapper = maleWrapper;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public Character getcWrapper() {
        return cWrapper;
    }

    public void setcWrapper(Character cWrapper) {
        this.cWrapper = cWrapper;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Double getScoreWrapper() {
        return scoreWrapper;
    }

    public void setScoreWrapper(Double scoreWrapper) {
        this.scoreWrapper = scoreWrapper;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public Float getGradeWrapper() {
        return gradeWrapper;
    }

    public void setGradeWrapper(Float gradeWrapper) {
        this.gradeWrapper = gradeWrapper;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public Set<String> getBooks() {
        return books;
    }

    public void setBooks(Set<String> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", ageWrapper=" + ageWrapper +
                ", male=" + male +
                ", maleWrapper=" + maleWrapper +
                ", c=" + c +
                ", cWrapper=" + cWrapper +
                ", score=" + score +
                ", scoreWrapper=" + scoreWrapper +
                ", grade=" + grade +
                ", gradeWrapper=" + gradeWrapper +
                ", children=" + children +
                ", books=" + books +
                '}';
    }
}
