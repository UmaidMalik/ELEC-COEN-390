package com.elec_coen_390.uvme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class UVHistoryActivity extends AppCompatActivity {

    ListView listViewUVHistory;
    View moreView;
    DatabaseHelper db;
    List<UVReadings> uvList;
    int index = 0;
    UVHistoryActivity.CustomAdapter customAdapter;
    Intent intent;

    private int SELECT = 0;

    private final int SELECT_TABLE_ALL_UV_DATA = 0;
    private final int SELECT_TABLE_UV_GRAPH_TABLE = 1;
    
    //******* New spinner for UV lIST
    private Spinner spinnerUVList;

    public static String UV_TABLE_PREFS = "uv_table_prefs";
    public static String UV_TABLE_ID = "uv_table_id";


    public static SharedPreferences selectTablePreferences;
    public static SharedPreferences.Editor editor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // this.getSupportActionBar().hide();
        getSupportActionBar().setTitle("UV History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_uv_history);
        TextView title = (TextView) findViewById(R.id.activityUVHistory);
        setupBottomNavigationListener();

        spinnerUVList = (Spinner) findViewById(R.id.spinnerUVHistory);

        uvList = new ArrayList<>();
        db = new DatabaseHelper(this);
        db.getReadableDatabase();

        selectTablePreferences = getSharedPreferences(UV_TABLE_PREFS, MODE_PRIVATE);
        SELECT = selectTablePreferences.getInt(UV_TABLE_ID, 0);


        startNotificationsThread(getCurrentFocus());


        //******* New Spinner: UV List
        setupUVListSpinner();



    }

    //******** New Spinner: UV list
    private void setupUVListSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.UVList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUVList.setAdapter(adapter);
        spinnerUVList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.light_blue));

                selectTablePreferences = getSharedPreferences(UV_TABLE_PREFS, MODE_PRIVATE);
                editor = getSharedPreferences(UV_TABLE_PREFS, MODE_PRIVATE).edit();

                switch (i) {

                    case SELECT_TABLE_ALL_UV_DATA:
                        SELECT = SELECT_TABLE_ALL_UV_DATA;
                        editor.putInt(UV_TABLE_ID, SELECT);
                        editor.apply();
                        break;
                    case SELECT_TABLE_UV_GRAPH_TABLE:
                        SELECT = SELECT_TABLE_UV_GRAPH_TABLE;
                        editor.putInt(UV_TABLE_ID, SELECT);
                        editor.apply();
                        break;
                    default:
                        SELECT = 0;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        selectTablePreferences = getSharedPreferences(UV_TABLE_PREFS, MODE_PRIVATE);
        SELECT = selectTablePreferences.getInt(UV_TABLE_ID, 0);

        switch(SELECT) {
            case SELECT_TABLE_ALL_UV_DATA:
                uvList = db.getAllUVData();
                break;
            case SELECT_TABLE_UV_GRAPH_TABLE:
                uvList = db. getUVGraphInfoTable();
                break;
        }
        setupListView();
    }

    private void startNotificationsThread(View v) {
        UVHistoryActivity.RunnableList runnableList = new UVHistoryActivity.RunnableList();
        new Thread(runnableList).start();
    }


    private class RunnableList implements Runnable {


        @Override
        public void run() {
            while (true) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        switch(SELECT) {
                            case SELECT_TABLE_ALL_UV_DATA:
                                uvList = db.getAllUVData();
                                break;
                            case SELECT_TABLE_UV_GRAPH_TABLE:
                                uvList = db. getUVGraphInfoTable();
                                break;
                        }
                        customAdapter.notifyDataSetChanged();
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setupListView() {
        listViewUVHistory = (ListView) findViewById(R.id.sensorDataListView);

        customAdapter = new UVHistoryActivity.CustomAdapter();
        listViewUVHistory.setAdapter(customAdapter);
    }

    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return uvList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            moreView = getLayoutInflater().inflate(R.layout.activity_history_row_data, null);

            TextView uvIndex = moreView.findViewById(R.id.textViewHistoryUVIndex);
            TextView timeStamp = moreView.findViewById(R.id.textViewHistoryTimeStamp);


            switch(SELECT) {
                case SELECT_TABLE_ALL_UV_DATA:
                    uvIndex.setText("UV: "+ uvList.get(i).uvToString());
                    break;
                case SELECT_TABLE_UV_GRAPH_TABLE:
                    uvIndex.setText(String.valueOf("Avg: " + uvList.get(i).getUv_avg() + "  Max: " + uvList.get(i).getUv_max())); // @TODO remove
                    break;
            }
            timeStamp.setText(uvList.get(i).timeStampToString());


            setListViewIcons(i);


            return moreView;
        }
    }

    private void setListViewIcons(int position) {
        ImageView colorImage = moreView.findViewById(R.id.imageViewSunIcon);
        ImageView warning = moreView.findViewById(R.id.warning);
        ImageView goToInfo = moreView.findViewById(R.id.goToInfo);

        float uvIndexValue = 0;
        switch(SELECT) {
            case SELECT_TABLE_ALL_UV_DATA:
                uvIndexValue = uvList.get(position).getUv_value();
                break;
            case SELECT_TABLE_UV_GRAPH_TABLE:
                uvIndexValue = uvList.get(position).getUv_max();
                break;
        }



        if (uvIndexValue < 1) {
            colorImage.setImageResource(R.drawable.ic_sunlight_default_level1_lightblue);
            warning.setImageResource(R.drawable.ic_extreme_condition);

        } else if (uvIndexValue >= 1 && uvIndexValue < 3) {
            colorImage.setImageResource(R.drawable.ic_sunlight_default_level1_lightblue);
            warning.setImageResource(R.drawable.ic_extreme_condition);

        } else if (uvIndexValue >= 3 && uvIndexValue < 6) {
            warning.setImageResource(R.drawable.ic_extreme_condition);
            colorImage.setImageResource(R.drawable.ic_sunlight_level2);


        } else if (uvIndexValue >= 6 && uvIndexValue < 8) {
            colorImage.setImageResource(R.drawable.ic_sunlight_level3);
            warning.setImageResource(R.drawable.ic_extreme_condition);

        } else if (uvIndexValue >= 8 && uvIndexValue < 11) {
            colorImage.setImageResource(R.drawable.ic_sunlight_level4);
            warning.setImageResource(R.drawable.ic_baseline_warning_24);
            goToInfo.setImageResource(R.drawable.ic_history_extreme);
            goToInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(getApplicationContext(), InfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            });

        } else if (uvIndexValue > 11) {
            colorImage.setImageResource(R.drawable.ic_sunlight_level5);
            warning.setImageResource(R.drawable.ic_baseline_warning_24);
            goToInfo.setImageResource(R.drawable.ic_history_extreme);
            goToInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(getApplicationContext(), InfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goToMoreActivity();
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

    protected void goToProfileActivity() {
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        intentProfile.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentProfile);
    }

    protected void goToMoreActivity() {
        Intent intentMore = new Intent(this, MoreActivity.class);
        intentMore.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentMore);
    }

    protected void goToMainActivity() {
        Intent intentMain = new Intent(this, MainActivity.class);
        intentMain.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentMain);
    }

    protected void goToGraphActivity() {
        Intent intentGraph = new Intent(this, DayGraph.class);
        intentGraph.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentGraph);
    }

    protected void goToYearGraph() {
        Intent intentYearGraph = new Intent(this, YearGraph.class);
        intentYearGraph.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentYearGraph);
    }

    protected void goToMonthGraph() {
        Intent intentGraphMonth = new Intent(this, MonthGraph.class);
        intentGraphMonth.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentGraphMonth);
    }

    public boolean onCreateOptionsMenu(Menu menu) { // access menu created.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.uv_history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.showDay) {
            goToGraphActivity();
            return true;
        }
        if (item.getItemId() == R.id.showYear) {
            goToYearGraph();
            return true;
        }
        if (item.getItemId() == R.id.showMonth) {
            goToMonthGraph();
            return true;
        }
        if (item.getItemId() == R.id.backToBottom) {
            listViewUVHistory.setSelection(customAdapter.getCount() - 1);

            return true;
        }

        if ( item.getItemId() == R.id.BTT) {
            listViewUVHistory.setSelectionAfterHeaderView();
            return true;
        }
        return super.onOptionsItemSelected(item);


    }


}