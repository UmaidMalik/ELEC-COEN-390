package com.elec_coen_390.uvme;

import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UVSensorData {

    private static float UVIntensity = 0.0f;

    private long uvId;

    private int day;
    private int month;
    private int year;
    private int second;
    private int minute;
    private int hour;

    private float uv_value;

    private float uv_max;
    private float uv_avg;

    public UVSensorData(float UV_Intensity) {
        UVIntensity = UV_Intensity;
    }


    public UVSensorData(long uvId, float uv_value, int hour, int minute, int second, int day, int month, int year) {
        this.uvId = uvId;
        this.day = day;
        this.month = month;
        this.year = year;
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.uv_value = uv_value;
    }

    public UVSensorData(long uvId, float uv_max, float uv_avg, int hour, int minute, int second, int day, int month, int year) {
        this.uvId = uvId;
        this.day = day;
        this.month = month;
        this.year = year;
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.uv_max = uv_max;
        this.uv_avg = uv_avg;
    }

    public static float getUVIntensity() {

        return UVIntensity;
    }

    public static void setUVIntensity(float UV_Intensity) {

        UVIntensity = UV_Intensity;
    }

    public String uvToString() {
        return String.valueOf(uv_value);
    }

    public float getUv_max() {
        return uv_max;
    }

    public float getUv_avg() {
        return uv_avg;
    }

    public int getDay() {
        return day;
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

    public int getHour() {
        return hour;
    }

    public float getUv_value() {
        return uv_value;
    }

    public String timeStampToString(){
        return hour + ":" + minute + ":" + second + " " + day + "/" + month + "/" + year;
    }


}
