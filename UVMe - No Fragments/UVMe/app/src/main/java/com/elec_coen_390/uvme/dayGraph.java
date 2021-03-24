package com.elec_coen_390.uvme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;
import java.text.NumberFormat;
import java.util.List;

public class dayGraph extends AppCompatActivity {
    public LineGraphSeries<DataPoint> series1, series2;
    public PointsGraphSeries<DataPoint> series3;
    TextView avgUV, maxUV;
    String date2 = "";
    DatabaseHelper db;

    Button dayButton, weekButton;
    private Context activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_u_vgraph);
        setupBottomNavigationListener();
        avgUV = findViewById(R.id.avgUV);
        maxUV = findViewById(R.id.maxUV);
        Intent intent = getIntent(); // lets us go back and forth from app to app
        showDay();
    }

    protected void showDay() { // this function needs to be changed
        // styling series
        GraphView graph = (GraphView) findViewById(R.id.graph);

        int[] xArray = new int[24]; // X AXIS

        double[] yArray = new double[]{1, 2, 5.33, 4.1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}; // this needs to be swapped out for database info


        int n = yArray.length;
        final double average = average(yArray, n); // USED TO FIND AVERAGE UVI LEVEL FROM DATABASE ( SOON )
        NumberFormat nm = NumberFormat.getNumberInstance();
        avgUV.setText(nm.format(average(yArray, n)));

        // CREATES X AXIS
        for (int i = 0; i < xArray.length; i++) {
            xArray[i] = i;
        }

        max(yArray);
        maxUV.setText(String.valueOf(max(yArray)));
        series1 = new LineGraphSeries<>(new DataPoint[]{ // SERIES ONE SHOWS USER A LINE GRAPH
                new DataPoint(xArray[0], yArray[0]),
                new DataPoint(xArray[1], yArray[1]),
                new DataPoint(xArray[2], yArray[2]),
                new DataPoint(xArray[3], yArray[3]),
                new DataPoint(xArray[4], yArray[4]),
                new DataPoint(xArray[5], yArray[5]),
                new DataPoint(xArray[6], yArray[6]),
                new DataPoint(xArray[7], yArray[7]),
                new DataPoint(xArray[8], yArray[8]),
                new DataPoint(xArray[9], yArray[9]),
                new DataPoint(xArray[10], yArray[10]),
                new DataPoint(xArray[11], yArray[11]),
                new DataPoint(xArray[12], yArray[12]),
                new DataPoint(xArray[13], yArray[13]),
                new DataPoint(xArray[14], yArray[14]),
                new DataPoint(xArray[15], yArray[15]),
                new DataPoint(xArray[16], yArray[16]),
                new DataPoint(xArray[17], yArray[17]),
                new DataPoint(xArray[18], yArray[18]),
                new DataPoint(xArray[19], yArray[19]),
                new DataPoint(xArray[20], yArray[20]),
                new DataPoint(xArray[21], yArray[21]),
                new DataPoint(xArray[22], yArray[22]),
                new DataPoint(xArray[23], yArray[23])});

        series3 = new PointsGraphSeries<>(new DataPoint[]{ // SERIES 3 SHOWS THE USER A DOT GRAPH FROM SERIES 1
                new DataPoint(xArray[0], yArray[0]),
                new DataPoint(xArray[1], yArray[1]),
                new DataPoint(xArray[2], yArray[2]),
                new DataPoint(xArray[3], yArray[3]),
                new DataPoint(xArray[4], yArray[4]),
                new DataPoint(xArray[5], yArray[5]),
                new DataPoint(xArray[6], yArray[6]),
                new DataPoint(xArray[7], yArray[7]),
                new DataPoint(xArray[8], yArray[8]),
                new DataPoint(xArray[9], yArray[9]),
                new DataPoint(xArray[10], yArray[10]),
                new DataPoint(xArray[11], yArray[11]),
                new DataPoint(xArray[12], yArray[12]),
                new DataPoint(xArray[13], yArray[13]),
                new DataPoint(xArray[14], yArray[14]),
                new DataPoint(xArray[15], yArray[15]),
                new DataPoint(xArray[16], yArray[16]),
                new DataPoint(xArray[17], yArray[17]),
                new DataPoint(xArray[18], yArray[18]),
                new DataPoint(xArray[19], yArray[19]),
                new DataPoint(xArray[20], yArray[20]),
                new DataPoint(xArray[21], yArray[21]),
                new DataPoint(xArray[22], yArray[22]),
                new DataPoint(xArray[23], yArray[23])});
        graph.addSeries(series1);
        graph.addSeries(series3);

        series3.setOnDataPointTapListener(new OnDataPointTapListener() { // ALLOWS USER TO SEE NODES
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getApplicationContext(), "UV Intensity" + dataPoint, Toast.LENGTH_LONG).show();
            }
        });
        // GRAPH SETUP
        graph.setTitle("DAY OVERVIEW"); // TITLE
        graph.setTitleTextSize(100);
        graph.setTitleColor(Color.WHITE);


        graph.getGridLabelRenderer().setVerticalAxisTitle("UVI"); // AXIS
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(50);

        graph.getGridLabelRenderer().setHorizontalAxisTitle("TIME (HOURS)");
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(75); // ANGLE OF AXIS
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graph.getViewport().setScrollable(true);  // activate horizontal scrolling
        graph.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScrollableY(true);
        // SETTING BOUNDS
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(11);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(24);
        graph.getGridLabelRenderer().setNumHorizontalLabels(24);
    }

    static double average(double[] a, int n) // FUNCTION RETURNS AVERAGE VALUE
    {// Find sum of array element
        double sum = 0;
        for (int i = 0; i < n; i++)
            sum += a[i];
        return sum / n;
    }

    static double max(double[] a) { // function to find max UVI
        double max = 0;
        for (int i = 0; i < a.length; i++) { // FUNCTION USED TO FIND MAX UVI LEVEL OF ENTIRE DAY
            for (int counter = 1; counter < a.length; counter++) {
                if (a[counter] > max) {
                    max = a[counter];
                }
            }
        }
        return max;
    }

    public Context getActivity() {
        Context activity = null;
        return activity;
    }

    public void setActivity(Context activity) {
        this.activity = activity;
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

}



