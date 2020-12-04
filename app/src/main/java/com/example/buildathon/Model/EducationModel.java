package com.example.buildathon.Model;

import java.io.Serializable;

public class EducationModel implements Serializable {
    String school , degree , field , start_date , end_date , grade , description;

    public EducationModel() {
    }

    public EducationModel(String school, String degree, String field, String start_date, String end_date, String grade, String description) {
        this.school = school;
        this.degree = degree;
        this.field = field;
        this.start_date = start_date;
        this.end_date = end_date;
        this.grade = grade;
        this.description = description;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
