package com.elec_coen_390.uvme;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DayGraph extends AppCompatActivity{

    public LineGraphSeries<DataPoint> seriesLineMax;
    public PointsGraphSeries<DataPoint> seriesPointsMax;
    public LineGraphSeries<DataPoint> seriesLineAvg;
    public PointsGraphSeries<DataPoint> seriesPointsAvg;
    private DatePickerDialog.OnDateSetListener  mDateSetLister;
    TextView avgUV;
    TextView maxUV;
    TextView selectedDate,chooseDateTextView;
    DatePickerDialog datePicker;
    DatabaseHelper dbGraph;
    List<UVReadings> uvList;
    GraphView graph;
    DataPoint[] dataPointsMAX;
    DataPoint[] dataPointsAVG;
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_u_vgraph);
        setupBottomNavigationListener();
        avgUV = findViewById(R.id.avgUV);
        maxUV = findViewById(R.id.maxUV);
        selectedDate = findViewById(R.id.selectedDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        graphSetup(); // graph setup is called on resume to abide by the program execution
        setDate(); // set date is also set up so the user can select new dates continously.

    }

    protected void graphSetup() {
        // styling series
        graph = (GraphView) findViewById(R.id.graph);

        // making a point at the origin so the graph exists before the user selects a date.
        // Line graphs -> how the graph looks like
        // Point graph -> important nodes ( MAX values )
        seriesLineMax = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0)
        });
        seriesPointsMax = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0)
        });

        seriesLineAvg = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0)
        });

        seriesPointsAvg = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0)
        });

        // adds the graph to the UI
        graph.addSeries(seriesLineMax);
        graph.addSeries(seriesPointsMax);
        graph.addSeries(seriesLineAvg);
        graph.addSeries(seriesPointsAvg);

        // Setting up the graph Legend
        seriesLineMax.setTitle("Max UV Readings");
        seriesLineMax.setBackgroundColor(Color.BLUE);
        seriesPointsMax.setTitle("Max Value");
        seriesPointsMax.setColor(Color.WHITE);
        seriesLineAvg.setTitle("Avg UV Readings");
        seriesLineAvg.setBackgroundColor(Color.RED);
        seriesPointsAvg.setTitle("Avg Value");
        seriesPointsAvg.setColor(Color.WHITE);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        // Setting up the graphs UI ( the axis and lables)
        graph.setTitle("Day Overview");
        graph.setTitleTextSize(100);
        graph.setTitleColor(0xFF03DAC5);
        graph.getGridLabelRenderer().setVerticalAxisTitle("UVI");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(0xFF03DAC5);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(50);
        graph.getGridLabelRenderer().setVerticalLabelsColor(0xFFB1D4E0);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(0xFFB1D4E0);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(0xFFB1D4E0);
        graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(50);
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(0xFF03DAC5);
        graph.getGridLabelRenderer().setGridColor(0xFFB1D4E0);
        graph.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graph.getViewport().setScrollable(true);  // activate horizontal scrolling
        graph.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScrollableY(true);
        graph.getGridLabelRenderer().setHumanRounding(false);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();

        // Setting up bounds for the day graph and creating a subdivision for the axis to show clear results.
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(18);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(26);
        graph.getGridLabelRenderer().setNumHorizontalLabels(14);
        graph.getGridLabelRenderer().setNumVerticalLabels(2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToUVHistoryActivity();
    }
    // intents used to go back and forth in the application.
    protected void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    protected void goToMoreActivity() {
        Intent intent = new Intent(this, MoreActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    protected void goToUVHistoryActivity() {
        Intent intent = new Intent(this, UVHistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    protected void goToProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    // bottom navigation call ( setup )
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

    // function used to pop up a date dialog so the user can select which date of data they wish to see.
    // The user can cancel out and the graph will be empty.
    protected void setDate() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePicker = new DatePickerDialog(DayGraph.this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int yearOfCentury, int monthOfYear, int dayOfMonth) {
                String date = (dayOfMonth+"/"+(monthOfYear+1) +"/"+yearOfCentury);
                graph.getGridLabelRenderer().setHorizontalAxisTitle("Date: "+ date); // setting up the title of the X axis (date)
                selectedDate.setText(date);
                // if the user selects the date displayed they can access the dialog again.
                selectedDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDate();
                    }
                });
                // sends the date selected to the next function which will display the Database values.
                getUVReadingFromDate(dayOfMonth, monthOfYear, yearOfCentury);
                }
            }, year , month, day);
                 datePicker.show();


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getUVReadingFromDate(int selectedDay_, int selectedMonth_, int selectedYear_) {

        uvList = new ArrayList<>();
        dbGraph = new DatabaseHelper(this);
        dbGraph.getReadableDatabase();
        uvList = dbGraph.getUVGraphInfo(); // taking from MAX table
        DecimalFormat df = new DecimalFormat("#,###,##0.00");
        selectedDay = selectedDay_;
        selectedMonth = selectedMonth_ +1;
        selectedYear = selectedYear_;
        int currentHour;
        float maxAverageUV = 0;
        int putHour;
        LinkedHashMap<Integer, Float> averagesMax = new LinkedHashMap<>(); // linked hash map used to organize the datapoints for database.
        LinkedHashMap<Integer, Float> maxes = new LinkedHashMap<>();
        int countSize = 0;
        float sum = 0;
        float averageOfHour = 0;

        for (int i = 0; i < uvList.size(); i++) {
            // finds which row in the database to look for and sets the values.
            if (selectedDay == uvList.get(i).getDay() &&
                    selectedMonth == uvList.get(i).getMonth() &&
                    selectedYear == uvList.get(i).getYear() ) {
                currentHour = uvList.get(i).getHour();
                putHour = uvList.get(i).getHour();

                // with the selected hour, we iterate to find the max -> go through entire list.
                while (currentHour == uvList.get(i).getHour() && i < uvList.size() - 1) {
                    if (maxAverageUV < uvList.get(i).getUv_avg()) {
                        maxAverageUV = uvList.get(i).getUv_avg();
                        putHour = uvList.get(i).getHour();
                    }
                    sum += uvList.get(i).getUv_avg();
                    countSize++;
                    i++;
                }

                averageOfHour = sum/countSize;
                // to put maxAverageUV in list
                maxes.put(putHour, maxAverageUV);
                averagesMax.put(putHour, averageOfHour);
                sum = 0;
                countSize = 0;
                maxAverageUV = 0; // reset the max;
            }

        }
        dataPointsMAX = new DataPoint[maxes.size()];
        int count = 0;
        // iterate through the LinkedHashMap and get the key(hour), value(max of the hour) and put to DataPoint(x, y)
        for (Map.Entry<Integer, Float> entry : maxes.entrySet()) {
            int key = entry.getKey();
            float value = entry.getValue();
            dataPointsMAX[count] = new DataPoint( key, Double.parseDouble(df.format(value)));;
            count++;
        }

        dataPointsAVG = new DataPoint[averagesMax.size()];
        count = 0;
        // iterate through the linked HashMap and get the key(hour), value(max of the hour) and put to DataPoint(x, y)
        for (Map.Entry<Integer, Float> entry : averagesMax.entrySet()) {
            int key = entry.getKey();
            float value = entry.getValue();
            dataPointsAVG[count] = new DataPoint( key, Double.parseDouble(df.format(value)));;
            count++;
        }



        //reset data points so there are no repeats when user changes the date.
       seriesPointsMax.resetData( new DataPoint[] {});
       seriesLineMax.resetData( new DataPoint[] {});
       seriesPointsAvg.resetData( new DataPoint[] {});
       seriesLineAvg.resetData( new DataPoint[] {});

        seriesPointsMax = new PointsGraphSeries<>(dataPointsMAX);
        seriesLineMax = new LineGraphSeries<>(dataPointsMAX);
        seriesPointsAvg = new PointsGraphSeries<>(dataPointsAVG);
        seriesLineAvg = new LineGraphSeries<>(dataPointsAVG);
        seriesLineMax.setTitle("Max UV readings");
        seriesLineMax.setColor(Color.BLUE);
        seriesPointsMax.setColor(Color.WHITE);
        seriesPointsMax.setTitle("Max data points");
        seriesPointsAvg.setTitle("Avg data points");
        seriesLineAvg.setTitle("AVG UV readings");
        seriesLineAvg.setColor(Color.RED);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        graph.removeAllSeries();

        graph.addSeries(seriesPointsMax); // adds the graph to the UI
        graph.addSeries(seriesLineMax);
        graph.addSeries(seriesPointsAvg);
        graph.addSeries(seriesLineAvg);

        // when the user selects on a node, it will display the max value and the hour it was set at.
        seriesPointsMax.setOnDataPointTapListener(new OnDataPointTapListener() { // ALLOWS USER TO SEE NODES
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                avgUV.setText(String.valueOf(dataPoint.getX()));
                maxUV.setText(String.valueOf(dataPoint.getY()));}
        });

        seriesPointsAvg.setOnDataPointTapListener(new OnDataPointTapListener() { // ALLOWS USER TO SEE NODES
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                avgUV.setText(String.valueOf(dataPoint.getX()));
                maxUV.setText(String.valueOf(dataPoint.getY()));}

        });
    }
}