package com.example.buildathon.Model;

public class SkillModel {
    String skill_name , stars;

    public SkillModel() {
    }

    public SkillModel(String skill_name, String stars) {
        this.skill_name = skill_name;
        this.stars = stars;
    }

    public String getSkill_name() {
        return skill_name;
    }

    public void setSkill_name(String skill_name) {
        this.skill_name = skill_name;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }
}
