package com.elec_coen_390.uvme;

import java.util.Calendar;

public class UvReadings {
    private int uvId;
    Calendar currentDateTime =Calendar.getInstance();
    int day = currentDateTime.get(Calendar.DAY_OF_MONTH);
    int month = currentDateTime.get(Calendar.MONTH);
    int year = currentDateTime.get(Calendar.YEAR);
    int second = currentDateTime.get(Calendar.SECOND);
    int minute = currentDateTime.get(Calendar.MINUTE);
    int hour = currentDateTime.get(Calendar.HOUR);
    private double uv_time;
    private float uv_value;
    private String date;
    /*
    public UvReadings ( double uv_time, float uv_value, String date) {
        this.uv_time = uv_time;
        this.uv_value = uv_value;
        this.date = date;
    }

     */
    public UvReadings ( int uv_d,int uv_hr, float uv_value) {
        this.day = uv_d;
        this.hour = uv_hr;
        this.uv_value = uv_value;
    }
    public void setUVDay(int uvDay) {
        this.day = uvDay;
    }
    public int getUVDay() {
      return day;
    }
    public void setUVhour(int hr) {
        this.hour = hr;
    }
    public int getUVhour() {
        return hour;
    }
    public void setUv(float uv) {
        this.uv_value = uv;

    }
    public float getUv() {
        return uv_value;
    }


/*
    public void setUvId(int uvId) {
        this.uvId = uvId;
    }


    public int getUvId() {
        return uvId;
    }


    public void setUvTime(double time) {

        this.uv_time = time;
    }


    public double getUvTime() {
        return uv_time;
    }


    public void setUv(float uv) {
        this.uv_value = uv;

    }


    public float getUv() {
        return uv_value;
    }


    public void setDate(String date) {
        this.date = date;
    }


    public String getDate() {
        return date;
    }


 */
}

