package com.rsrit.rcrm.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WorkExperience {

    @Id
    private ObjectId _id;
    private String employer;
    private String jobTitle;
    private CustomDate startDate;
    private CustomDate endDate;

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy-MM-dd").create();
        return gson.toJson(this);
    }

    public WorkExperience() {
        this._id = ObjectId.get();
    }

    public WorkExperience(ObjectId _id, String employer, String jobTitle, CustomDate startDate, CustomDate endDate) {
        super();
        this._id = _id;
        this.employer = employer;
        this.jobTitle = jobTitle;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public CustomDate getStartDate() {
        return startDate;
    }

    public void setStartDate(CustomDate startDate) {
        this.startDate = startDate;
    }

    public CustomDate getEndDate() {
        return endDate;
    }

    public void setEndDate(CustomDate endDate) {
        this.endDate = endDate;
    }

}
