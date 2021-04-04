package com.elec_coen_390.uvme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UVHistoryActivity extends AppCompatActivity {
Button showGraphButton;
Button showWeekGraphButton;
Button showMonthGraphButton;
ListView sensorDataListView;
DatabaseHelper dbHelper;
private float uvIndex = 0.00f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_uv_history);
        TextView title = (TextView) findViewById(R.id.activityUVHistory);
        title.setText("UV History");
        setupBottomNavigationListener();
        showMonthGraphButton=findViewById(R.id.showMonthGraphButton);
        showGraphButton=findViewById(R.id.showGraphButton);
        showWeekGraphButton=findViewById(R.id.showWeekGraphButton);
        sensorDataListView=(ListView)findViewById(R.id.sensorDataListView);

       // UVSensorData.getUVIntensity();
        Calendar currentDateTime = Calendar.getInstance();
        int day = currentDateTime.get(Calendar.DAY_OF_MONTH);
        int month = currentDateTime.get(Calendar.MONTH);
        int year = currentDateTime.get(Calendar.YEAR);
        int second = currentDateTime.get(Calendar.SECOND);
        int minute = currentDateTime.get(Calendar.MINUTE);
        int hour = currentDateTime.get(Calendar.HOUR);

        /*
        dbHelper= new DatabaseHelper(this);
        UvReadings uv = new UvReadings(-1,year,month,day,hour,minute,second,UVSensorData.getUVIntensity());
        dbHelper.insertUV(uv);
        //loadTime();
         */


        showWeekGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToWeekGraph();
            }
        });
        showGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGraphActivity();
            }
        });
        showMonthGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMonthGraph();
            }
        });

    }
   /* void loadTime() {
        List<UvReadings> times = dbHelper.getAllUVData(); // creates a list of time soted in database
        List<String> timeStringList = new ArrayList<>();
        for (int i = 0; i < 10 ; i++) {
            timeStringList.add(times.get(i).toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeStringList);
        sensorDataListView.setAdapter(adapter);
    }


    */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMoreActivity();
    }
    private void setupBottomNavigationListener() {

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        // Menu items are left unselected
        bottomNavigationView.getMenu().getItem(0).setCheckable(false);
        bottomNavigationView.getMenu().getItem(1).setCheckable(false);
        bottomNavigationView.getMenu().getItem(2).setCheckable(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_home:
                        goToMainActivity();
                        break;

                    case R.id.action_profile:
                        goToProfileActivity();
                        break;

                    case R.id.action_more:

                        break;
                }
                return false;
            }
        });

    }
    protected void goToProfileActivity() {
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        startActivity(intentProfile);
        finish();
    }

    protected void goToMoreActivity() {
        Intent intentMore = new Intent(this, MoreActivity.class);
        startActivity(intentMore);
        finish();
    }
    protected void goToMainActivity() {
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
        finish();
    }
    protected void goToGraphActivity() {
        Intent intentGraph = new Intent(this, DayGraph.class);
        startActivity(intentGraph);
        finish();
    }
    protected  void goToWeekGraph(){
        Intent intentGraphWeek = new Intent(this, weekGraph.class);
        startActivity(intentGraphWeek);
        finish();
    }
    protected  void goToMonthGraph(){
        Intent intentGraphMonth = new Intent(this, monthGraph.class);
        startActivity(intentGraphMonth);
        finish();
    }
}