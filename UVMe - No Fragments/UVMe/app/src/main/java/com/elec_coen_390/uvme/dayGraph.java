package com.elec_coen_390.uvme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.elec_coen_390.uvme.profileAtributes.eyeColor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class dayGraph extends AppCompatActivity {
    public LineGraphSeries<DataPoint> series1,series2;
    public PointsGraphSeries<DataPoint> series3;
    TextView avgUV,maxUV;

    Button dayButton,weekButton;
    private Context activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_u_vgraph);
        setupBottomNavigationListener();
        avgUV=findViewById(R.id.avgUV);
        maxUV=findViewById(R.id.maxUV);
        Intent intent = getIntent(); // lets us go back and forth from app to app
        showDay();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToUVHistoryActivity();
    }
    protected void goToMainActivity() {
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
        finish();
    }
    protected void goToMoreActivity() {
        Intent intentMore = new Intent(this, MoreActivity.class);
        startActivity(intentMore);
        finish();
    }
    protected void goToUVHistoryActivity() {
        Intent intentHistory = new Intent(this, UVHistoryActivity.class);
        startActivity(intentHistory);
        finish();
    }
    protected void goToProfileActivity() {
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        startActivity(intentProfile);
        finish();
    }
    private void setupBottomNavigationListener() {

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2); // bottom navigation menu index item {0(Profile),1(Home),2(More)}
        menuItem.setChecked(true);

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

    protected  void showDay(){ // this function needs to be changed
        // styling series
        GraphView graph = (GraphView) findViewById(R.id.graph);
        int arr[];
        arr = new int[24];
        double max = arr[0];
        for (int i=0; i<24;i++){
            Random rand = new Random();
            int rand_int1 = rand.nextInt(11);
            if (rand_int1!=0) {
                arr[i] = rand_int1;
            }
            for (int counter = 1; counter < arr.length; counter++)
            {    if (arr[counter] > max)
            { max = arr[counter]; }
                maxUV.setText(String.valueOf(max));
            }
            double average = rand_int1/arr.length;
            avgUV.setText(String.valueOf(average));
        }
        series1 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(2, 6),
                new DataPoint(4, 5),
                new DataPoint(6, 4),
                new DataPoint(8, 3),
                new DataPoint(14, 3),
                new DataPoint(18, 8),
                new DataPoint(21, 1),
        });
        series3=new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(2, 6),
                new DataPoint(4, 5),
                new DataPoint(6, 4),
                new DataPoint(8, 3),
                new DataPoint(14, 3),
                new DataPoint(18, 8),
                new DataPoint(21, 1),
        });
        graph.addSeries(series1);
        graph.addSeries(series3);

        graph.setTitle("DAY OVERVIEW");
        graph.setTitleTextSize(100);
        graph.setTitleColor(Color.WHITE);

        graph.getGridLabelRenderer().setVerticalAxisTitle("UVI");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(50);
       // graph.getGridLabelRenderer().setHorizontalAxisTitle("TIME");
        //graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(75);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graph.getViewport().setScrollable(true);  // activate horizontal scrolling
        graph.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(11);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(24);
        graph.getGridLabelRenderer().setNumHorizontalLabels(24);
    }


    public Context getActivity() {
        Context activity = null;
        return activity;
    }

    public void setActivity(Context activity) {
        this.activity = activity;
    }
}
