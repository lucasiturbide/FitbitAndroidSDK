package com.mindbodyonline.fitbitintegration.service.models;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Distance {

    @Expose
    private String date;
    @Expose
    private Double value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

}
