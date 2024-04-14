package com.demo.Properties;

import java.util.List;

public class DemoProps {

    private String name;

    private Integer age;

    private List<String> interests2;

    private List<Skill> skills;

    public static class Skill {
        private String skill1;
        private String skill2;

        public Skill() {
        }

        public Skill(String skill1, String skill2) {
            this.skill1 = skill1;
            this.skill2 = skill2;
        }

        public String getSkill1() {
            return skill1;
        }

        public void setSkill1(String skill1) {
            this.skill1 = skill1;
        }

        public String getSkill2() {
            return skill2;
        }

        public void setSkill2(String skill2) {
            this.skill2 = skill2;
        }

        @Override
        public String toString() {
            return "Skill{" +
                    "skill1='" + skill1 + '\'' +
                    ", skill2='" + skill2 + '\'' +
                    '}';
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<String> getInterests2() {
        return interests2;
    }

    public void setInterests2(List<String> interests2) {
        this.interests2 = interests2;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "DemoProps{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", interests2=" + interests2 +
                ", skills=" + skills +
                '}';
    }
}
