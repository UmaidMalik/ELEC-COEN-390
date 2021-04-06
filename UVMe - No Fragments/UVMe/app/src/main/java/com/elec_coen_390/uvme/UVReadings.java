package com.elec_coen_390.uvme;

import java.util.Calendar;

public class UVReadings {
    private long uvId;

    private int day;
    private int month;
    private int year;
    private int second;
    private int minute;
    private int hour;

    private float uv_value;

    public String timeStampToString(){
        return hour + ":" + minute + ":" + second + " " + day + "/" + month + "/" + year;
    }

    public String uvToString() {
        return String.valueOf(uv_value);
    }

    public UVReadings(long uvId, float uv_value, int hour, int minute, int second, int day, int month, int year) {
        this.uvId = uvId;
        this.day = day;
        this.month = month;
        this.year = year;
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.uv_value = uv_value;
    }

    public long getUvId() {
        return uvId;
    }

    public void setUvId(long uvId) {
        this.uvId = uvId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public float getUv_value() {
        return uv_value;
    }

    public void setUv_value(float uv_value) {
        this.uv_value = uv_value;
    }
}

