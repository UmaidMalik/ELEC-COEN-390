package com.elec_coen_390.uvme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UVHistoryActivity extends AppCompatActivity {
Button showGraphButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_uv_history);
        TextView title = (TextView) findViewById(R.id.activityUVHistory);
        title.setText("UV History");
        showGraphButton=findViewById(R.id.showGraphButton);

        showGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGraphActivity();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMoreActivity();
    }

    protected void goToProfileActivity() {
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        startActivity(intentProfile);
        finish();
    }

    protected void goToMoreActivity() {
        Intent intentMore = new Intent(this, MoreActivity.class);
        startActivity(intentMore);
        finish();
    }
    protected void goToMainActivity() {
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
        finish();
    }
    protected void goToGraphActivity() {
        Intent intentGraph = new Intent(this, UVgraph.class);
        startActivity(intentGraph);
        finish();
    }
}