package com.rsrit.rcrm.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Education {
    @Id
    private ObjectId _id;
    private String school;
    private String degree;
    private Integer yearCompleted;
    private String majorStudy;
    private String minorStudy;
    private Double gpa;
    private String country;
    private String state;
    private String city;

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy-MM-dd").create();
        return gson.toJson(this);
    }

    public Education() {
        this._id = ObjectId.get();
    }

    public Education(ObjectId _id, String school, String degree, Integer yearCompleted, String majorStudy, String minorStudy, Double gpa, String country, String state, String city) {
        super();
        this._id = _id;
        this.school = school;
        this.degree = degree;
        this.yearCompleted = yearCompleted;
        this.majorStudy = majorStudy;
        this.minorStudy = minorStudy;
        this.gpa = gpa;
        this.country = country;
        this.state = state;
        this.city = city;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
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

    public Integer getYearCompleted() {
        return yearCompleted;
    }

    public void setYearCompleted(Integer yearCompleted) {
        this.yearCompleted = yearCompleted;
    }

    public String getMajorStudy() {
        return majorStudy;
    }

    public void setMajorStudy(String majorStudy) {
        this.majorStudy = majorStudy;
    }

    public String getMinorStudy() {
        return minorStudy;
    }

    public void setMinorStudy(String minorStudy) {
        this.minorStudy = minorStudy;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
