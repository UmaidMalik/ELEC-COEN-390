package com.elec_coen_390.uvme;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MonthGraph extends AppCompatActivity {
    public LineGraphSeries<DataPoint> seriesLineMax;
    public PointsGraphSeries<DataPoint> seriesPointsMax;

    public LineGraphSeries<DataPoint> seriesLineAvg;
    public PointsGraphSeries<DataPoint> seriesPointsAvg;

    private DatePickerDialog.OnDateSetListener  mDateSetLister;

    TextView avgUV;
    TextView maxUV;
    TextView selectedDate, chooseDateTextView;
    DatePickerDialog datePicker;

    DatabaseHelper dbGraph;
    List<UVReadings> uvList;
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");


    GraphView graph;
    DataPoint[] dataPointsMAX;
    DataPoint[] dataPointsAVG;

    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_graph);
        this.getSupportActionBar().hide();

        setupBottomNavigationListener();

        selectedDate = findViewById(R.id.selectedDate);

        avgUV=findViewById(R.id.avgUV);
        maxUV=findViewById(R.id.maxUV);

        graphSetup();
        setDate();

    }

    protected void graphSetup() {
        // styling series
        graph = (GraphView) findViewById(R.id.graphMonth);

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


        graph.addSeries(seriesLineMax); // adds the graph to the UI
        graph.addSeries(seriesPointsMax);

        graph.addSeries(seriesLineAvg);
        graph.addSeries(seriesPointsAvg);



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

        graph.setTitle("DAY OVERVIEW"); // TITLE
        graph.setTitleTextSize(100);
        graph.setTitleColor(0xFFB1D4E0); // lightBlue
        graph.getGridLabelRenderer().setVerticalAxisTitle("UVI"); // AXIS
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(0xFFB1D4E0);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(50);
        graph.getGridLabelRenderer().setVerticalLabelsColor(0xFFB1D4E0);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(0xFFB1D4E0);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(75); // ANGLE OF AXIS
        graph.getGridLabelRenderer().setGridColor(0xFFB1D4E0);
        graph.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graph.getViewport().setScrollable(true);  // activate horizontal scrolling
        graph.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScrollableY(true);

        graph.getGridLabelRenderer().setHumanRounding(false);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();

        // SETTING BOUNDS
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(18);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(32);
        graph.getGridLabelRenderer().setNumHorizontalLabels(7);
        graph.getGridLabelRenderer().setNumVerticalLabels(2);
    }

    protected void setDate() { // Used to date the date with calendar
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePicker = new DatePickerDialog(MonthGraph.this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int yearOfCentury, int monthOfYear, int dayOfMonth) {
                String date = ((monthOfYear+1) +"/"+yearOfCentury);
                selectedDate.setText(date);
                selectedDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDate();
                    }
                });
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
        selectedMonth = selectedMonth_ + 1;
        selectedYear = selectedYear_;




        int currentDay;
        float maxAverageUV = 0;
        int putDay;
        LinkedHashMap<Integer, Float> averagesMax = new LinkedHashMap<>();
        int j;
        for (int i = 0; i < uvList.size(); i++) {

            if (selectedMonth == uvList.get(i).getMonth() &&
                    selectedYear == uvList.get(i).getYear() ) {
                currentDay = uvList.get(i).getDay();
                putDay = uvList.get(i).getDay();
                j = i; //
                while (currentDay == uvList.get(j).getDay() && j < uvList.size() - 1) { // with the selected day, we iterate to find the max in the month

                    if (maxAverageUV < uvList.get(j).getUv_avg()) {
                        maxAverageUV = uvList.get(j).getUv_avg();
                        putDay = uvList.get(j).getDay();
                    }


                    j++;
                }
                // to put maxAverageUV in list
                averagesMax.put(putDay, maxAverageUV);
                maxAverageUV = 0; // reset the max;
                i = j;

            }

        }
        avgUV.setText(String.valueOf(averagesMax.size()));

        dataPointsMAX = new DataPoint[averagesMax.size()];
        int count = 0;


        // iterate through the LinkedHashMap and get
        for (Map.Entry<Integer, Float> entry : averagesMax.entrySet()) {
            int key = entry.getKey();
            float value = entry.getValue();
            DataPoint pointMax = new DataPoint(key, Double.parseDouble(df.format(value)));
            dataPointsMAX[count] = pointMax;
            count++;
        }










        seriesPointsMax.resetData( new DataPoint[] {});
        seriesLineMax.resetData( new DataPoint[] {});
        seriesPointsAvg.resetData( new DataPoint[] {});
        seriesLineAvg.resetData( new DataPoint[] {});

       /*

        dataPointsMAX = new DataPoint[countSize];
        dataPointsAVG = new DataPoint[countSize];



         count = 0;
        for (int i = 0; i < uvList.size(); i++) {
            if (selectedDay == uvList.get(i).getDay() &&
                    selectedMonth == uvList.get(i).getMonth() &&
                    selectedYear == uvList.get(i).getYear()
                ) {


                int x = uvList.get(i).getMinute();

                float yMax  =  uvList.get(i).getUv_max();
                float yAvg = uvList.get(i).getUv_avg();

                DataPoint pointMax = new DataPoint(count, Double.parseDouble(df.format(yMax)));
                DataPoint pointAvg = new DataPoint(count, Double.parseDouble(df.format(yAvg)));


                dataPointsMAX[count] = pointMax;
                dataPointsAVG[count] = pointAvg;
                count++;
            }

        }



         */





        seriesPointsMax = new PointsGraphSeries<>(dataPointsMAX);
        seriesLineMax = new LineGraphSeries<>(dataPointsMAX);

        //seriesPointsAvg = new PointsGraphSeries<>(dataPointsAVG);
        // seriesLineAvg = new LineGraphSeries<>(dataPointsAVG);

        seriesLineMax.setTitle("Max UV Readings");

        seriesLineMax.setColor(Color.BLUE);
        seriesPointsMax.setColor(Color.WHITE);
        seriesPointsMax.setTitle("Max data points");

        // seriesLineAvg.setTitle("Avg UV Readings");

        // seriesLineAvg.setColor(Color.RED);
        //seriesPointsAvg.setShape(PointsGraphSeries.Shape.RECTANGLE);
        // seriesPointsAvg.setColor(Color.GRAY);
        //seriesPointsAvg.setTitle("Avg data points");

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        graph.removeAllSeries();

        graph.addSeries(seriesPointsMax); // adds the graph to the UI
        graph.addSeries(seriesLineMax);

        //graph.addSeries(seriesPointsAvg);
        //graph.addSeries(seriesLineAvg);

        seriesPointsMax.setOnDataPointTapListener(new OnDataPointTapListener() { // ALLOWS USER TO SEE NODES
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getApplicationContext(), "\t\t\t  UV Intensity \n [HOUR,INTENSITY] \n" +"\t\t\t\t\t"+ dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

        seriesPointsAvg.setOnDataPointTapListener(new OnDataPointTapListener() { // ALLOWS USER TO SEE NODES
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getApplicationContext(), "\t\t\t  UV Intensity \n [HOUR,INTENSITY] \n" +"\t\t\t\t\t"+ dataPoint, Toast.LENGTH_SHORT).show();
            }
        });


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

}