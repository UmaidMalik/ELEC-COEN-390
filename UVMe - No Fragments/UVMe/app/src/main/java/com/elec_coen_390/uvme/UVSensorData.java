package com.elec_coen_390.uvme;

import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UVSensorData {

    private static float UVIntensity = 0.0f;

    public UVSensorData(float UV_Intensity) {
        UVIntensity = UV_Intensity;
    }

    public static float getUVIntensity() {

        return UVIntensity;
    }

    public static void setUVIntensity(float UV_Intensity) {

        UVIntensity = UV_Intensity;
    }


}
