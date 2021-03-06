package com.elec_coen_390.uvme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class UVSensorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_uv_sensor);



        TextView title = (TextView) findViewById(R.id.activityUVSensor);
        title.setText("UV Sensor");
    }
}