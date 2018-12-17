package com.rsrit.rcrm.model;

import java.util.Date;

import com.rsrit.rcrm.util.DateHelper;

public class CustomDate {

    private Date date;
    private long ticks;

    public CustomDate(Date date, long ticks) {
        this.date = date;
        this.ticks = ticks;
    }

    public CustomDate() {
        this.date = new Date();
        this.ticks = DateHelper.getUTCTicks(date);
    }

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
