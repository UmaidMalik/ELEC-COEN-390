package com.elec_coen_390.uvme;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class UVHistoryActivity extends AppCompatActivity {
    Button showGraphButton;
    Button showWeekGraphButton;
    Button showMonthGraphButton;
    ListView listViewUVHistory;

    View moreView;

    DatabaseHelper db;
    List<UVReadings> uvList;
    int selection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_uv_history);
        TextView title = (TextView) findViewById(R.id.activityUVHistory);
        title.setText("UV History");
        setupBottomNavigationListener();
        showMonthGraphButton=findViewById(R.id.showMonthGraphButton);
        showGraphButton=findViewById(R.id.showGraphButton);
        showWeekGraphButton=findViewById(R.id.showWeekGraphButton);

        showWeekGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToWeekGraph();
            }
        });
        showGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGraphActivity();
            }
        });
        showMonthGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goToMonthGraph();
                //listViewUVHistory.smoothScrollToPosition(0);
                listViewUVHistory.setSelectionAfterHeaderView();
            }
        });

        uvList = new ArrayList<>();
        db = new DatabaseHelper(this);
        db.getReadableDatabase();


        startNotificationsThread(getCurrentFocus());

    }

    @Override
    protected void onResume() {
        super.onResume();
        uvList = db.getAllUVData();
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

        UVHistoryActivity.CustomAdapter customAdapter = new UVHistoryActivity.CustomAdapter();
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


            uvIndex.setText(uvList.get(i).uvToString());
            timeStamp.setText(uvList.get(i).timeStampToString());

            setListViewIcons(i);




            return moreView;
        }
    }

    private void setListViewIcons(int position) {
        ImageView colorImage = moreView.findViewById(R.id.imageViewSunIcon);
        float uvIndexValue = uvList.get(position).getUv_value();

        if (uvIndexValue  < 1) {
            colorImage.setImageResource(R.drawable.ic_sunlight_default_level1_lightblue);
        }
        else if (uvIndexValue  >= 1 && uvIndexValue  < 3) {
            colorImage.setImageResource(R.drawable.ic_sunlight_default_level1_lightblue);
        }
        else if (uvIndexValue  >= 3 && uvIndexValue  < 6) {
            colorImage.setImageResource(R.drawable.ic_sunlight_level2);
        }
        else if (uvIndexValue  >= 6 && uvIndexValue  < 8) {
            colorImage.setImageResource(R.drawable.ic_sunlight_level3);
        }
        else if (uvIndexValue  >= 8 && uvIndexValue  < 11) {
            colorImage.setImageResource(R.drawable.ic_sunlight_level4);
        }
        else if (uvIndexValue  > 11){
            colorImage.setImageResource(R.drawable.ic_sunlight_level5);
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
    protected  void goToWeekGraph(){
        Intent intentGraphWeek = new Intent(this, weekGraph.class);
        intentGraphWeek.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentGraphWeek);
    }
    protected  void goToMonthGraph(){
        Intent intentGraphMonth = new Intent(this, monthGraph.class);
        intentGraphMonth.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentGraphMonth);
    }
}