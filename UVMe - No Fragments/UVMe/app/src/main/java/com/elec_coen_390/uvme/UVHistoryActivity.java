package com.elec_coen_390.uvme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class UVHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_uv_history);



        TextView title = (TextView) findViewById(R.id.activityUVHistory);
        title.setText("UV History");
    }
}