package com.mindbodyonline.fitbitintegration.service.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Tracker {

    @SerializedName("distance")
    @Expose
    private Distance_ distance;
    @SerializedName("floors")
    @Expose
    private Floors floors;
    @SerializedName("steps")
    @Expose
    private Steps steps;

    /**
     * @return The distance
     */
    public Distance_ getDistance() {
        return distance;
    }

    /**
     * @param distance The distance
     */
    public void setDistance(Distance_ distance) {
        this.distance = distance;
    }

    /**
     * @return The floors
     */
    public Floors getFloors() {
        return floors;
    }

    /**
     * @param floors The floors
     */
    public void setFloors(Floors floors) {
        this.floors = floors;
    }

    /**
     * @return The steps
     */
    public Steps getSteps() {
        return steps;
    }

    /**
     * @param steps The steps
     */
    public void setSteps(Steps steps) {
        this.steps = steps;
    }

}
