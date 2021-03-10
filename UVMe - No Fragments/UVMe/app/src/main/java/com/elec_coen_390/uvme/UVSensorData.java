package com.elec_coen_390.uvme;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UVSensorData {

    private float UVIntensity;
    private ArrayList<Date> readingTimes;
    private ArrayList<String> readingTimesFormatted;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");


    public UVSensorData(float UVIntensity, ArrayList<Date> readingTimes) {

        this.UVIntensity = UVIntensity;

        this.readingTimes = readingTimes;
        formatReadTimes(readingTimes);
    }

    private void formatReadTimes(ArrayList<Date> times) {
        readingTimesFormatted = new ArrayList<String>();
        for (int i=0; i < 5; i++) {
            readingTimesFormatted.add(dateFormatter.format(readingTimes.get(i)));
        }
    }

    public ArrayList<Date> getReadingTimes() {

        return this.readingTimes;
    }

    public void setReadingTimes(ArrayList<Date> readingTimes) {

        this.readingTimes = readingTimes;
    }

    public float getUVIntensity() {

        return UVIntensity;
    }

    public void setUVIntensity(float UVIntensity) {

        this.UVIntensity = UVIntensity;
    }
}
