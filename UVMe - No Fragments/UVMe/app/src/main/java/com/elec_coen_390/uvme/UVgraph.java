package com.elec_coen_390.uvme;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class UVgraph extends AppCompatActivity {

    public LineGraphSeries<DataPoint> series1,series2;
    public PointsGraphSeries<DataPoint> series3;
    TextView avgUV,maxUV;
    Spinner timeSpinner;
    private Context activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_u_vgraph);
        timeSpinner = findViewById(R.id.timeSpinner);
        Intent intent = getIntent(); // lets us go back and forth from app to app
        final Spinner timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
        timeSpinner.setEnabled(true);
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(UVgraph.this, R.array.Time, R.layout.color_spinner_layour);
        timeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.getBackground().setColorFilter(getResources().getColor(R.color.light_blue), PorterDuff.Mode.SRC_ATOP);
        timeSpinner.setBackgroundColor(Color.BLACK);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int TimePossition, long arg3) {}
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
        avgUV=findViewById(R.id.avgUV);
        maxUV=findViewById(R.id.maxUV);
        showGraph();
       //week();
       // showGraph();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToUVHistoryActivity();
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

    protected  void showGraph(){ // this function needs to be changed
        // styling series
        double x=0;
        int numDataPoints=24;
        GraphView graph = (GraphView) findViewById(R.id.graph);
        int arr[];
        int temp[];
        arr=new int[24];
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
                new DataPoint(2, arr[0]),
                new DataPoint(4, arr[1]),
                new DataPoint(6, arr[2]),
                new DataPoint(8, arr[3]),
                new DataPoint(14, arr[4]),
                new DataPoint(18, arr[5]),
                new DataPoint(21, arr[6]),
        });
        series3=new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(2, arr[0]),
                new DataPoint(4, arr[1]),
                new DataPoint(6, arr[2]),
                new DataPoint(8, arr[3]),
                new DataPoint(14, arr[4]),
                new DataPoint(18, arr[5]),
                new DataPoint(21, arr[6]),

        });

        graph.addSeries(series1);
        graph.addSeries(series3);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        graph.setTitle("DAY OVERVIEW");
        graph.setTitleTextSize(100);
        graph.setTitleColor(Color.WHITE);

        graph.getGridLabelRenderer().setVerticalAxisTitle("UVI");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(50);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("TIME");



        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
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

    protected  void week(){
        // generate Dates
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d5 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d6 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d7 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d8 = calendar.getTime();
        GraphView graph = (GraphView) findViewById(R.id.graph);

// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()
        int arr[];
        int temp[];
        arr=new int[7];
        double max = arr[0];
        for (int i=0; i<7;i++){
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
                new DataPoint(d1, arr[0]),
                new DataPoint(d2, arr[1]),
                new DataPoint(d3, arr[2]),
                new DataPoint(d4, arr[3]),
                new DataPoint(d5, arr[4]),
                new DataPoint(d6, arr[5]),
                new DataPoint(d7, arr[6]),
        });
        series3=new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(d1, arr[0]),
                new DataPoint(d2, arr[1]),
                new DataPoint(d3, arr[2]),
                new DataPoint(d4, arr[3]),
                new DataPoint(d5, arr[4]),
                new DataPoint(d6, arr[5]),
                new DataPoint(d7, arr[6]),


});

        graph.addSeries(series1);
        graph.addSeries(series3);
        graph.setTitle("WEEK OVERVIEW");
        graph.setTitleTextSize(100);
        graph.setTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalAxisTitle("UVI");

        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(50);

// set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(7); // only 4 because of the space
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(75);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graph.getViewport().setScrollable(true);  // activate horizontal scrolling
        graph.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScrollableY(true);


// set manual x bounds to have nice steps
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(11);
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d7.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

    }

    public Context getActivity() {
        Context activity = null;
        return activity;
    }

    public void setActivity(Context activity) {
        this.activity = activity;
    }
}
