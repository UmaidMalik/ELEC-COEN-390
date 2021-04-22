package com.elec_coen_390.uvme.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.elec_coen_390.uvme.Services.Database.DatabaseHelper;
import com.elec_coen_390.uvme.R;
import com.elec_coen_390.uvme.UVSensorData;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MonthGraph extends AppCompatActivity {
    public LineGraphSeries<DataPoint> seriesLineMax;
    public PointsGraphSeries<DataPoint> seriesPointsAvg;
    public PointsGraphSeries<DataPoint> seriesPointsMax;
    public LineGraphSeries<DataPoint> seriesLineAvg;
    private DatePickerDialog.OnDateSetListener mDateSetLister;
    TextView avgUV;
    TextView maxUV;
    TextView selectedDate, chooseDateTextView;
    DatePickerDialog datePicker;
    DatabaseHelper dbGraph;
    List<UVSensorData> uvList;
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
        avgUV = findViewById(R.id.avgUV);
        maxUV = findViewById(R.id.maxUV);
        selectedDate = findViewById(R.id.selectedDate);

        setDate();
        graphSetup();

    }

    protected void graphSetup() {
        graph = (GraphView) findViewById(R.id.graphMonth);
        seriesLineMax = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0)
        });
        seriesPointsMax = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0)
        });
        seriesLineAvg = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0)
        });
        seriesPointsAvg = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0)
        });
        // adding series to the graph
        graph.addSeries(seriesLineMax);
        graph.addSeries(seriesPointsMax);
        graph.addSeries(seriesLineAvg);
        graph.addSeries(seriesPointsAvg);
        // setting legend up
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
        // setting up the graph UI
        graph.setTitle("Month Overview"); // TITLE
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        graph.setTitleTextSize(100);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(50);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(0xFFB1D4E0);
        graph.getViewport().setScrollable(true);  // activate horizontal scrolling
        graph.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        graph.setTitleColor(0xFF03DAC5); // lightBlue
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(0xFF03DAC5);
        graph.getGridLabelRenderer().setVerticalAxisTitle("UVI"); // AXIS
        graph.getGridLabelRenderer().setVerticalLabelsColor(0xFFB1D4E0);
        //graph.getGridLabelRenderer().setHorizontalLabelsAngle(75); // ANGLE OF AXIS
        graph.getGridLabelRenderer().setGridColor(0xFFB1D4E0);
        graph.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graph.getViewport().setScrollableY(true);
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(0xFFB1D4E0);
        graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(50);
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(0xFF03DAC5);

        // SETTING BOUNDS
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(18);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(33);
        graph.getGridLabelRenderer().setNumVerticalLabels(6);
        graph.getGridLabelRenderer().setNumHorizontalLabels(17);
    }
    // function to set the month, user can select a different month to see datapoints.
    protected void setDate() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePicker = new DatePickerDialog(MonthGraph.this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int yearOfCentury, int monthOfYear, int dayOfMonth) {
                String date = ((monthOfYear + 1) + "/" + yearOfCentury);
                graph.getGridLabelRenderer().setHorizontalAxisTitle("Days of the month"); // AXIS
                selectedDate.setText(date);
                selectedDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDate();
                    }
                });
                getUVReadingFromDate(dayOfMonth, monthOfYear, yearOfCentury); // sends date to function to get database values.
            }
        }, year, month, day);
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
        LinkedHashMap<Integer, Float> maxes = new LinkedHashMap<>();
        LinkedHashMap<Integer, Float> averagesMax = new LinkedHashMap<>();
        int countSize = 0;
        float sum = 0;
        float averageOfDay = 0;

        // same algorithm used as day graph but we do not include the hour, looking for only month day year.
        for (int i = 0; i < uvList.size(); i++) {
            if (selectedMonth == uvList.get(i).getMonth() &&
                    selectedYear == uvList.get(i).getYear()) {
                currentDay = uvList.get(i).getDay();
                putDay = uvList.get(i).getDay();

                while (currentDay == uvList.get(i).getDay() && i < uvList.size() - 1) { // with the selected day, we iterate to find the max in the month
                    if (maxAverageUV < uvList.get(i).getUv_avg()) {
                        maxAverageUV = uvList.get(i).getUv_avg();
                        putDay = uvList.get(i).getDay();
                    }
                    sum += uvList.get(i).getUv_avg();
                    countSize++;
                    i++;
                }
                averageOfDay = sum/countSize;
                // to put maxAverageUV in list
                maxes.put(putDay, maxAverageUV);
                averagesMax.put(putDay, averageOfDay);
                sum = 0;
                countSize = 0;
                maxAverageUV = 0; // reset the max;

            }
        }
        dataPointsMAX = new DataPoint[maxes.size()];
        int count = 0;
        // iterate through the LinkedHashMap and get key (day) and value (month), put to DataPoint(x, y);
        for (Map.Entry<Integer, Float> entry : maxes.entrySet()) {
            int key = entry.getKey();
            float value = entry.getValue();
            dataPointsMAX[count] = new DataPoint(key, Double.parseDouble(df.format(value)));
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

        seriesPointsMax.resetData(new DataPoint[]{});
        seriesLineMax.resetData(new DataPoint[]{});
        seriesPointsAvg.resetData(new DataPoint[]{});
        seriesLineAvg.resetData(new DataPoint[]{});
        seriesPointsMax = new PointsGraphSeries<>(dataPointsMAX);
        seriesLineMax = new LineGraphSeries<>(dataPointsMAX);
        seriesPointsAvg = new PointsGraphSeries<>(dataPointsAVG);
        seriesLineAvg = new LineGraphSeries<>(dataPointsAVG);

        // setting up the output
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
        graph.addSeries(seriesPointsAvg); // adds the graph to the UI
        graph.addSeries(seriesLineAvg);
        // if user taps on the node it will output the value of day and UV reading.
        seriesPointsMax.setOnDataPointTapListener(new OnDataPointTapListener() { // ALLOWS USER TO SEE NODES
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                avgUV.setText(String.valueOf(dataPoint.getX()));
                maxUV.setText(String.valueOf(dataPoint.getY()));
            }
        });

        seriesPointsAvg.setOnDataPointTapListener(new OnDataPointTapListener() { // ALLOWS USER TO SEE NODES
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                avgUV.setText(String.valueOf(dataPoint.getX()));
                maxUV.setText(String.valueOf(dataPoint.getY()));
            }
        });
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
}
