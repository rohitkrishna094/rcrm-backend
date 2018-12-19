package com.rsrit.rcrm.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rsrit.rcrm.serialization.CustomDateSerializer;
import com.rsrit.rcrm.util.DateHelper;

public class CustomDate {

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "en_US")
    @JsonDeserialize(using = CustomDateSerializer.class)
    private Date date;
    private long ticks;

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy-MM-dd").create();
        return gson.toJson(this);
    }

    public CustomDate(Date date, long ticks) {
        this.date = date;
        this.ticks = ticks;
    }

    @JsonCreator
    public CustomDate(Date date) {
        this.date = date;
        this.ticks = DateHelper.getUTCTicks(this.date);
    }

    public CustomDate() {

    }

    // public CustomDate() {
    // // this.date = new Date();
    // // this.ticks = DateHelper.getUTCTicks(date);
    // System.out.println("hello");
    // }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTicks() {
        return ticks;
    }

    public void setTicks(long ticks) {
        this.ticks = ticks;
    }

}
