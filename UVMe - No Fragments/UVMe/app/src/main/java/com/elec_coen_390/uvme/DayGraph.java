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
import java.util.List;

public class DayGraph extends AppCompatActivity {

    public LineGraphSeries<DataPoint> series1, series2;
    public PointsGraphSeries<DataPoint> series3;
    TextView avgUV;
    TextView maxUV;
    DatePickerDialog datePicker;
    String date2 = "";
    DatabaseHelper dbGraph;
    List<UVReadings> uvList;
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    Calendar c = Calendar.getInstance();
    private Context activity;
    private float uvIndex = 0.00f;

    GraphView graph;
    DataPoint[] dataPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_u_vgraph);
        setupBottomNavigationListener();
        avgUV = findViewById(R.id.avgUV);
        maxUV = findViewById(R.id.maxUV);
        Intent intent = getIntent(); // lets us go back and forth from app to app


        graphSetup();
        setDate();

    }



    protected void graphSetup() {
        // styling series
        graph = (GraphView) findViewById(R.id.graph);

        series1 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0)
        });

        series3 = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0)
        }) ;




        graph.addSeries(series1); // adds the graph to the UI
        graph.addSeries(series3);




        series1.setTitle("UV Readings");
        series1.setBackgroundColor(Color.BLUE);
        series3.setTitle("Max Value");
        series1.setBackgroundColor(Color.RED);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        graph.setTitle("DAY OVERVIEW"); // TITLE
        graph.setTitleTextSize(100);
        series3.setColor(Color.RED);
        graph.setTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalAxisTitle("UVI"); // AXIS
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(50);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(75); // ANGLE OF AXIS
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graph.getViewport().setScrollable(true);  // activate horizontal scrolling
        graph.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScrollableY(true);





        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX){
                if(isValueX)
                {
                    return timeFormat.format(new Date((long) value));
                }
                else
                {
                    return super.formatLabel(value, isValueX);
                }}
        });



        graph.getGridLabelRenderer().setHumanRounding(false);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();

        // SETTING BOUNDS
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(20);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(24);
        graph.getGridLabelRenderer().setNumHorizontalLabels(12);
    }

    static double average(double[] a, int n) // FUNCTION RETURNS AVERAGE VALUE
    {// Find sum of array element
        double sum = 0;
        for (int i = 0; i < n; i++)
            sum += a[i];
        return sum / n;
    }

    static double max(float[] a) { // function to find max UVI
        float max = 0;
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

    protected void setDate() { // Used to date the date with calendar
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePicker = new DatePickerDialog(DayGraph.this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int yearOfCentury, int monthOfYear, int dayOfMonth) {
                getUVReadingFromDate(day, month, year);}}, year, month, day);
                 datePicker.show();}


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getUVReadingFromDate(/*final String date*/ int selectedDay, int selectedMonth, int selectedYear) {
        uvList = new ArrayList<>();
        dbGraph = new DatabaseHelper(this);
        dbGraph.getReadableDatabase();
        uvList = dbGraph.getUVGraphInfo(9, 4, 2021); // taking from MAX table
        DecimalFormat df = new DecimalFormat("#,###,##0.00");
        series1.setTitle("UV Readings");
        series1.setBackgroundColor(Color.BLUE);
        series3.setTitle("Max Value");
        series3.setColor(Color.RED);
        series1.setBackgroundColor(Color.RED);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        int countSize = 0;
        for (int i = 0; i < uvList.size(); i++)
            if (selectedDay == uvList.get(i).getDay() &&
                    selectedMonth + 1 == uvList.get(i).getMonth() &&
                    selectedYear == uvList.get(i).getYear() &&
                    uvList.get(i).getHour() == 3) {

                countSize++;
            }

        dataPoints = new DataPoint[countSize];
        for (int i = 0; i < uvList.size(); i++) {
            if (selectedDay == uvList.get(i).getDay() &&
                    selectedMonth + 1 == uvList.get(i).getMonth() &&
                    selectedYear == uvList.get(i).getYear() &&
                    uvList.get(i).getHour() == 3) {

                //int x = uvList.get(i).getHour();
                int x = uvList.get(i).getMinute(); //@TODO change to hour remember
                float y  =  uvList.get(i).getUv_max();
                DataPoint point = new DataPoint(x, Double.parseDouble(df.format(y)));
                dataPoints[i] = point;
            }

        }




        series3.resetData( new DataPoint[] {});
        series1.resetData( new DataPoint[] {});
        series3 = new PointsGraphSeries<>(dataPoints);
        series1 = new LineGraphSeries<>(dataPoints);

        graph.removeAllSeries();
        graph.addSeries(series3); // adds the graph to the UI
        graph.addSeries(series1);

        series3.setOnDataPointTapListener(new OnDataPointTapListener() { // ALLOWS USER TO SEE NODES
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getApplicationContext(), "\t\t\t  UV Intensity \n [HOUR,INTENSITY] \n" +"\t\t\t\t\t"+ dataPoint, Toast.LENGTH_LONG).show();
            }
        });

    }
    protected void maxVal(float x) {
        if (x < UVSensorData.getUVIntensity()) {
            x = UVSensorData.getUVIntensity();
        }
        if (x < UVSensorData.getUVIntensity()) {
            x = UVSensorData.getUVIntensity();
        }
    }
}