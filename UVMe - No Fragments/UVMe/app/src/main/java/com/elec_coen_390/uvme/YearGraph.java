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

    protected void yearGraphSetup(){
        // generate Dates

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
        yearGraph.setTitle("DAY OVERVIEW"); // TITLE
        yearGraph.setTitleTextSize(100);
        yearGraph.setTitleColor(0xFFB1D4E0); // lightBlue
        yearGraph.getGridLabelRenderer().setVerticalAxisTitle("UVI"); // AXIS
        yearGraph.getGridLabelRenderer().setVerticalAxisTitleColor(0xFFB1D4E0);
        yearGraph.getGridLabelRenderer().setVerticalAxisTitleTextSize(50);
        yearGraph.getGridLabelRenderer().setVerticalLabelsColor(0xFFB1D4E0);
        yearGraph.getGridLabelRenderer().setHorizontalLabelsColor(0xFFB1D4E0);
        yearGraph.getGridLabelRenderer().setHorizontalLabelsAngle(75); // ANGLE OF AXIS
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
        yearGraph.getViewport().setMaxX(12);
        yearGraph.getGridLabelRenderer().setNumHorizontalLabels(7);
        yearGraph.getGridLabelRenderer().setNumVerticalLabels(2);
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

    protected void setDate() { // Used to date the date with calendar
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePicker = new DatePickerDialog(YearGraph.this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int yearOfCentury, int monthOfYear, int dayOfMonth) {
                String date = (dayOfMonth+"/"+(monthOfYear+1) +"/"+yearOfCentury);
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

        int currentHour;
        float maxAverageUV = 0;
        int putHour;
        LinkedHashMap<Integer, Float> averagesMax = new LinkedHashMap<>();
        int j;
        for (int i = 0; i < uvList.size(); i++) {
            if (selectedDay == uvList.get(i).getDay() &&
                    selectedMonth == uvList.get(i).getMonth() &&
                    selectedYear == uvList.get(i).getYear() ) {
                currentHour = uvList.get(i).getHour();
                putHour = uvList.get(i).getHour();
                j = i; //
                while (currentHour == uvList.get(j).getHour() && j < uvList.size() - 1) { // with the selected hour, we iterate to find the max

                    if (maxAverageUV < uvList.get(j).getUv_avg()) {
                        maxAverageUV = uvList.get(j).getUv_avg();
                        putHour = uvList.get(j).getHour();
                    }


                    j++;
                }
                // to put maxAverageUV in list
                averagesMax.put(putHour, maxAverageUV);
                maxAverageUV = 0; // reset the max;
                i = j;

            }
            // if (selectedDay != uvList.get(i).getDay()) {
            //      continue;
            //  }
        }
        avgUVYear.setText(String.valueOf(averagesMax.size()));

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

        int countSize = 0;
        for (int i = 0; i < uvList.size(); i++) {
            if ( selectedDay == uvList.get(i).getDay() &&
                    selectedMonth == uvList.get(i).getMonth() &&
                    selectedYear == uvList.get(i).getYear()
            ) {

                countSize++;
            }
        }
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
                Toast.makeText(getApplicationContext(), "\t\t\t  UV Intensity \n [HOUR,INTENSITY] \n" +"\t\t\t\t\t"+ dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

        seriesPointsAvgYear.setOnDataPointTapListener(new OnDataPointTapListener() { // ALLOWS USER TO SEE NODES
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getApplicationContext(), "\t\t\t  UV Intensity \n [HOUR,INTENSITY] \n" +"\t\t\t\t\t"+ dataPoint, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
