package com.elec_coen_390.uvme;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class UvReadings {
    private int uvId;

    private double uv_time;
    private double uv_value;
    private String date;
    public UvReadings ( double uv_time, double uv_value, String date) {
        this.uv_time = uv_time;
        this.uv_value = uv_value;
        this.date = date;
    }
    /* set UVId*/
    public void setUvId(int uvId) {
        this.uvId = uvId;
    }

    /* Get UVId*/
    public int getUvId() {
        return uvId;
    }

    /*set UV time value */
    public void setUvTime(double time) {
        this.uv_time = time;
    }

    /*get UV time value*/
    public double getUvTime() {
        return uv_time;
    }

    /*set UV value */
    public void setUv(double uv) {
        this.uv_value = uv;

    }

    /*get UV value*/
    public double getUv() {
        return uv_value;
    }

    /*set current time and date */
    public void setDate(String date) {
        this.date = date;
    }

    /*get current time and date */
    public String getDate() {
        return date;
    }
}

