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
    private float uv_value;


    /*
    do this instead
     private long id;
    private String Year;
    private String Month;
    private String Day;
    private String Hour;
    private String Min;
    private String Sec;

 // need to add uv value to the toString();
    @Override
    public String toString() {
        return
                "Year: " + Year + '\'' + ",Month: " + Month
        +" Day: " + Day + '\'' + ",Hour: " + Hour+  ",Min: " + Min + '\'' + ",Sec: ";
    }
     */






    public UvReadings ( int id,int uv_yr,int uv_m, int uv_d,int uv_hr, int uv_min, int uv_sec, float uv_value) {
        this.uvId=id;
        this.day = uv_d;
        this.month = uv_m;
        this.year = uv_yr;
        this.second=uv_sec;
        this.minute=uv_min;
        this.hour = uv_hr;
        this.uv_value = uv_value;
    }

    public int getUvId() {
        return uvId;
    }

    public void setUvId(int uvId) {
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

    /*
    use this instead

     public Time(long id, String y, String m,String d, String hr,String min, String sec,float uv_value)
    {
        this.id = id;
       this.Month=m;
       this.Year=y;
       this.Day=d;
       this.Hour=hr;
       this.Min=m;
       this.Sec=sec;
       this.uv_value = uv_value;
    }

    public long getId() {
        return id;
    }


    public String getYear() {
        return Year;
    }

    public String getMonth() {
        return Month;
    }

    public String getDay() {
        return Day;
    }

    public String getHour() {
        return Hour;
    }

    public String getMin() {
        return Min;
    }

    public String getSec() {
        return Sec;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setYear(String year) {
        Year = year;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public void setDay(String day) {
        Day = day;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    public void setMin(String min) {
        Min = min;
    }

    public void setSec(String sec) {
        Sec = sec;
    }
    public float getUv_value() {
        return uv_value;
    }

    public void setUv_value(float uv_value) {
        this.uv_value = uv_value;
    }
     */
}

