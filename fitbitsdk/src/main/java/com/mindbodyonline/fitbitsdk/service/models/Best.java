package com.mindbodyonline.fitbitsdk.service.models;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Best {

    @Expose
    private Total total;
    @Expose
    private Tracker tracker;

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

}
