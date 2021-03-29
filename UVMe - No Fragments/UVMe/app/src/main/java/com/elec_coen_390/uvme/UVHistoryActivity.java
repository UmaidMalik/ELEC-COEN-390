package com.elec_coen_390.uvme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UVHistoryActivity extends AppCompatActivity {
Button showGraphButton;
Button showWeekGraphButton;
Button showMonthGraphButton;

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