package com.example.buildathon.Model;

public class ExperienceModel {
    String title , employment_type , company_name , location ,  start_date , end_date,description;

    public ExperienceModel() {
    }

    public ExperienceModel(String title, String employment_type, String company_name, String location, String start_date, String end_date, String description) {
        this.title = title;
        this.employment_type = employment_type;
        this.company_name = company_name;
        this.location = location;
        this.start_date = start_date;
        this.end_date = end_date;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmployment_type() {
        return employment_type;
    }

    public void setEmployment_type(String employment_type) {
        this.employment_type = employment_type;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
