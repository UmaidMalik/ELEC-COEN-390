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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YearGraph extends AppCompatActivity {
    public LineGraphSeries<DataPoint> seriesLineMaxYear;
    public PointsGraphSeries<DataPoint> seriesPointsMaxYear;
    public LineGraphSeries<DataPoint> seriesLineAvgYear;
    public PointsGraphSeries<DataPoint> seriesPointsAvgYear;

    private DatePickerDialog.OnDateSetListener  mDateSetLister;

    TextView avgUVYear;
    TextView maxUVYear;
    TextView selectedDateYear,chooseDateTextView;
    DatePickerDialog datePicker;
    String date2 = "";
    DatabaseHelper dbGraph;
    List<UVReadings> uvList;
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    private Context activity;
    private float uvIndex = 0.00f;

    GraphView yearGraph;
    DataPoint[] dataPointsMAX;
    DataPoint[] dataPointsAVG;

    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_year_graph);
        setupBottomNavigationListener();
        avgUVYear = findViewById(R.id.avgUVYear);
        maxUVYear = findViewById(R.id.maxUVYear);
        selectedDateYear = findViewById(R.id.selectedDateYear);
        yearGraphSetup();
        setDate();
    }



    protected void yearGraphSetup(){

        uvList = new ArrayList<>();
        dbGraph = new DatabaseHelper(this);
        dbGraph.getReadableDatabase();
        uvList = dbGraph.getUVGraphInfo(); // taking from MAX table

        yearGraph = (GraphView) findViewById(R.id.yearGraph);
        seriesLineMaxYear = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0)
        });
        seriesPointsMaxYear = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0)
        });
        seriesLineAvgYear = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0)
        });
        seriesPointsAvgYear = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0)
        });


        yearGraph.addSeries(seriesLineMaxYear); // adds the graph to the UI
        yearGraph.addSeries(seriesPointsMaxYear);
        yearGraph.addSeries(seriesLineAvgYear);
        yearGraph.addSeries(seriesPointsAvgYear);
        seriesLineMaxYear.setTitle("Max UV Readings");
        seriesLineMaxYear.setBackgroundColor(Color.BLUE);
        seriesPointsMaxYear.setTitle("Max Value");
        seriesPointsMaxYear.setColor(Color.WHITE);
        seriesLineAvgYear.setTitle("Avg UV Readings");
        seriesLineAvgYear.setBackgroundColor(Color.RED);
        seriesPointsAvgYear.setTitle("Avg Value");
        seriesPointsAvgYear.setColor(Color.WHITE);
        yearGraph.getLegendRenderer().setVisible(true);
        yearGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        yearGraph.setTitle("Year Overview"); // TITLE
        yearGraph.setTitleTextSize(100);
        yearGraph.setTitleColor(0xFF03DAC5); // lightBlue
        yearGraph.getGridLabelRenderer().setVerticalAxisTitle("UVI"); // AXIS
        yearGraph.getGridLabelRenderer().setVerticalAxisTitleColor(0xFF03DAC5);
        yearGraph.getGridLabelRenderer().setVerticalAxisTitleTextSize(50);
        yearGraph.getGridLabelRenderer().setVerticalLabelsColor(0xFFB1D4E0);


        yearGraph.getGridLabelRenderer().setHorizontalLabelsColor(0xFFB1D4E0);
        yearGraph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(50);
        yearGraph.getGridLabelRenderer().setHorizontalAxisTitleColor(0xFF03DAC5);

        yearGraph.getGridLabelRenderer().setGridColor(0xFFB1D4E0);
        yearGraph.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        yearGraph.getViewport().setScrollable(true);  // activate horizontal scrolling
        yearGraph.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        yearGraph.getViewport().setScrollableY(true);
        yearGraph.getGridLabelRenderer().setHumanRounding(false);
        GridLabelRenderer gridLabel = yearGraph.getGridLabelRenderer();

        // SETTING BOUNDS
        yearGraph.getViewport().setMinY(0);
        yearGraph.getViewport().setMaxY(18);
        yearGraph.getViewport().setMinX(0);
        yearGraph.getViewport().setMaxX(14);
        yearGraph.getGridLabelRenderer().setNumHorizontalLabels(8);
        yearGraph.getGridLabelRenderer().setNumVerticalLabels(4);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToUVHistoryActivity();
    }
    protected void goToUVHistoryActivity() {
        Intent intent = new Intent(this, UVHistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    protected void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    protected void goToProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    protected void goToMoreActivity() {
        Intent intent = new Intent(this, MoreActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
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
                        goToMoreActivity();
                        break;
                }
                return false;
            }
        });
    }

    protected void setDate() { // Used to date the date with calendar
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePicker = new DatePickerDialog(YearGraph.this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override

            public void onDateSet(DatePicker view, int yearOfCentury, int monthOfYear, int dayOfMonth) {
                String date = String.valueOf(yearOfCentury);
                yearGraph.getGridLabelRenderer().setHorizontalAxisTitle("Months of: "+date ); // AXIS
                selectedDateYear.setText(date);

                selectedDateYear.setOnClickListener(new View.OnClickListener() {
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
        selectedMonth = selectedMonth_ +1;
        selectedYear = selectedYear_;

        int currentMonth;
        float maxAverageUV = 0;
        int putMonth;
        LinkedHashMap<Integer, Float> averagesMax = new LinkedHashMap<>();
        int j;
        for (int i = 0; i < uvList.size(); i++) {
            if (selectedYear == uvList.get(i).getYear() ) {

                currentMonth = uvList.get(i).getMonth();
                putMonth = uvList.get(i).getMonth();

                j = i; //
                while (currentMonth == uvList.get(j).getMonth() && j < uvList.size() - 1) { // with the selected hour, we iterate to find the max

                    if (maxAverageUV < uvList.get(j).getUv_avg()) {
                        maxAverageUV = uvList.get(j).getUv_avg();
                        putMonth = uvList.get(j).getMonth();
                    }


                    j++;
                }
                // to put maxAverageUV in list
                averagesMax.put(putMonth, maxAverageUV);
                maxAverageUV = 0; // reset the max;
                i = j;

            }

        }


        dataPointsMAX = new DataPoint[averagesMax.size()];
        int count = 0;

        for (Map.Entry<Integer, Float> entry : averagesMax.entrySet()) {
            int key = entry.getKey();
            float value = entry.getValue();
            DataPoint pointMax = new DataPoint( key, Double.parseDouble(df.format(value)));
            //DataPoint pointMax = new DataPoint( count, Double.parseDouble(df.format(value)));
            dataPointsMAX[count] = pointMax;
            count++;
        }

        // this is where we are , we need to get day 13 working


        seriesPointsMaxYear.resetData( new DataPoint[] {});
        seriesLineMaxYear.resetData( new DataPoint[] {});
        seriesPointsAvgYear.resetData( new DataPoint[] {});
        seriesLineAvgYear.resetData( new DataPoint[] {});
        seriesPointsMaxYear = new PointsGraphSeries<>(dataPointsMAX);
        seriesLineMaxYear = new LineGraphSeries<>(dataPointsMAX);

        //seriesPointsAvg = new PointsGraphSeries<>(dataPointsAVG);
        // seriesLineAvg = new LineGraphSeries<>(dataPointsAVG);

        seriesLineMaxYear.setTitle("Max UV Readings");

        seriesLineMaxYear.setColor(Color.BLUE);
        seriesPointsMaxYear.setColor(Color.WHITE);
        seriesPointsMaxYear.setTitle("Max data points");

        // seriesLineAvg.setTitle("Avg UV Readings");

        // seriesLineAvg.setColor(Color.RED);
        //seriesPointsAvg.setShape(PointsGraphSeries.Shape.RECTANGLE);
        // seriesPointsAvg.setColor(Color.GRAY);
        //seriesPointsAvg.setTitle("Avg data points");

        yearGraph.getLegendRenderer().setVisible(true);
        yearGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        yearGraph.removeAllSeries();

        yearGraph.addSeries(seriesPointsMaxYear); // adds the graph to the UI
        yearGraph.addSeries(seriesLineMaxYear);

        //graph.addSeries(seriesPointsAvg);
        //graph.addSeries(seriesLineAvg);

        seriesPointsMaxYear.setOnDataPointTapListener(new OnDataPointTapListener() { // ALLOWS USER TO SEE NODES
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                avgUVYear.setText(String.valueOf(dataPoint.getX()));
                maxUVYear.setText(String.valueOf(dataPoint.getY()));
            }
        });

        seriesPointsAvgYear.setOnDataPointTapListener(new OnDataPointTapListener() { // ALLOWS USER TO SEE NODES
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getApplicationContext(), "\t\t\t  UV Intensity \n [DAY,INTENSITY] \n" +"\t\t\t\t\t"+ dataPoint, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
