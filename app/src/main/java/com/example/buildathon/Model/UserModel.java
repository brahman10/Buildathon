package com.example.buildathon.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserModel implements Serializable {
    String name , password, mobile , id , img_url , email , status;
    String title , location , description;
    ArrayList<EducationModel> education;
    ArrayList<AccomplishmentModel> project;
    ArrayList<ExperienceModel> experience;
    ArrayList<SkillModel> skill;


    public UserModel() {
    }

    public UserModel(String name, String password, String mobile, String id, String img_url, String email, String status, String title, String location, String description, ArrayList<EducationModel> education, ArrayList<AccomplishmentModel> project, ArrayList<ExperienceModel> experience, ArrayList<SkillModel> skill) {
        this.name = name;
        this.password = password;
        this.mobile = mobile;
        this.id = id;
        this.img_url = img_url;
        this.email = email;
        this.status = status;
        this.title = title;
        this.location = location;
        this.description = description;
        this.education = education;
        this.project = project;
        this.experience = experience;
        this.skill = skill;
    }

    public ArrayList<SkillModel> getSkill() {
        return skill;
    }

    public void setSkill(ArrayList<SkillModel> skill) {
        this.skill = skill;
    }

    public ArrayList<ExperienceModel> getExperience() {
        return experience;
    }

    public void setExperience(ArrayList<ExperienceModel> experience) {
        this.experience = experience;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<EducationModel> getEducation() {
        return education;
    }

    public void setEducation(ArrayList<EducationModel> education) {
        this.education = education;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ArrayList<AccomplishmentModel> getProject() {
        return project;
    }

    public void setProject(ArrayList<AccomplishmentModel> project) {
        this.project = project;
    }


}

