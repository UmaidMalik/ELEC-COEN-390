package com.elec_coen_390.uvme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elec_coen_390.uvme.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * This activity allows the user send feedback and issues via email to the developer
 *
 */

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_feedback);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // this is to prevent the keyboard from opening on startup since there is a EditText in this activity;)

        // editTextFeedbackName contains Name of sender
        EditText editTextFeedbackName = (EditText) findViewById(R.id.editTextFeedbackName);

        // editTextFeedbackDescription comprises the feedback text field
        EditText editTextFeedbackDescription = (EditText) findViewById(R.id.editTextFeedbackDescription);

        // button send feedback
        Button btn = (Button) findViewById(R.id.buttonSendFeedback);

        setupBottomNavigationListener();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/html");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "umaid54321@hotmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for UVMe App");
                intent.putExtra(Intent.EXTRA_TEXT, "Sender: " + editTextFeedbackName.getText() + "\n\n Message: " + editTextFeedbackDescription.getText());
                try {

                    startActivity(Intent.createChooser(intent, "Please select Email"));

                }
                catch (android.content.ActivityNotFoundException exception){

                    Toast.makeText(FeedbackActivity.this, "There are no Email Clients", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Navigation methods
    @Override
    public void onBackPressed() {

        goToMoreActivity();
    }

    protected void goToMainActivity() {
        Intent intentMain = new Intent(this, MainActivity.class);
        intentMain.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentMain);
    }

    protected void goToMoreActivity() {
        Intent intentMore = new Intent(this, MoreActivity.class);
        intentMore.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentMore);
    }

    protected void goToProfileActivity() {
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        intentProfile.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentProfile);
    }

    // bottom navigation ui setup
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

                    case R.id.action_profile:
                        item.setCheckable(true);
                        goToProfileActivity();
                        break;

                    case R.id.action_more:
                        item.setCheckable(true);
                        goToMoreActivity();;
                        break;

                    case R.id.action_home:
                        item.setCheckable(true);
                        goToMainActivity();
                        break;

                    default:

                }
                return false;
            }
        });
    }
}