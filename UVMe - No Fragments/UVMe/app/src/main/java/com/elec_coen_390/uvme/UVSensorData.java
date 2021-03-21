package com.elec_coen_390.uvme;

import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UVSensorData {

    private float UVIntensity;

    public UVSensorData(float UVIntensity) {
        this.UVIntensity = UVIntensity;
    }

    public float getUVIntensity() {

        return UVIntensity;
    }

    public void setUVIntensity(float UVIntensity) {

        this.UVIntensity = UVIntensity;
    }

}
