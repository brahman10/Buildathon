package com.example.buildathon.Model;

public class AccomplishmentModel {

    String name , start_date , end_date , description , creator,url;

    public AccomplishmentModel() {
    }

    public AccomplishmentModel(String name, String start_date, String end_date, String description, String creator, String url) {
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.description = description;
        this.creator = creator;
        this.url = url;
    }

    public String getCreator() {
        return creator;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
