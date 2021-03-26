package com.elec_coen_390.uvme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
public class monthGraph extends AppCompatActivity {
    public LineGraphSeries<DataPoint> lineGraphSeries;
    public PointsGraphSeries<DataPoint> dataPointPointsGraphSeries;
    private Context activity;
    TextView avgUV,maxUV;
    private float uvIndex = 0.00f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_graph);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_week_graph);
        setupBottomNavigationListener();
        avgUV=findViewById(R.id.avgUV);
        maxUV=findViewById(R.id.maxUV);
        Intent intent = getIntent(); // lets us go back and forth from app to app
        month();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToUVHistoryActivity();
    }
    protected void goToUVHistoryActivity() {
        Intent intentHistory = new Intent(this, UVHistoryActivity.class);
        startActivity(intentHistory);
        finish();
    }
    protected void month(){
        // generate Dates
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);

        GraphView graph = (GraphView) findViewById(R.id.graph);

        double []yArray=new double[]{1,2,5.33}; // this needs to be swapped out for database info
        double maxUVI = yArray[0];
        int n=yArray.length;

        final double average = average(yArray, n); // USED TO FIND AVERAGE UVI LEVEL FROM DATABASE ( SOON )
        NumberFormat nm = NumberFormat.getNumberInstance();
        avgUV.setText(nm.format(average(yArray,n)));
        max(yArray);
        maxUV.setText(String.valueOf(max(yArray)));
        lineGraphSeries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d1,  uvIndex = UVSensorData.getUVIntensity()),
                new DataPoint(d2, uvIndex = UVSensorData.getUVIntensity()),
                new DataPoint(d3, uvIndex = UVSensorData.getUVIntensity())});

        dataPointPointsGraphSeries =new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(d1,  uvIndex = UVSensorData.getUVIntensity()),
                new DataPoint(d2, uvIndex = UVSensorData.getUVIntensity()),
                new DataPoint(d3, uvIndex = UVSensorData.getUVIntensity())});

        graph.addSeries(lineGraphSeries);
        graph.addSeries(dataPointPointsGraphSeries);
        graph.setTitle("MONTH OVERVIEW");
        graph.setTitleTextSize(100);
        graph.setTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalAxisTitle("UVI");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(50);
        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("WEEK");
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(85);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graph.getViewport().setScrollable(true);  // activate horizontal scrolling
        graph.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScrollableY(true);

        // set manual x bounds to have nice steps
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(11);
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        dataPointPointsGraphSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getApplicationContext(), "UV Intensity"+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

        graph.getGridLabelRenderer().setHumanRounding(false);}
    static double average(double[] a, int n) // FUNCTION RETURNS AVERAGE VALUE
    {
        // Find sum of array element
        double sum = 0;
        for (int i = 0; i < n; i++)
            sum += a[i];

        return sum / n;
    }
    public Context getActivity() {
        Context activity = null;
        return activity;
    }
    public void setActivity(Context activity) {
        this.activity = activity;
    }
    protected void goToMainActivity() {
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
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
    static double max(double []a){ // function to find max UVI
        double max=0;
        for (int i = 0; i < a.length; i++) { // FUNCTION USED TO FIND MAX UVI LEVEL OF ENTIRE DAY
            for (int counter = 1; counter < a.length; counter++) {
                if (a[counter] > max) {
                    max = a[counter]; } } }
        return max;}
}