package com.elec_coen_390.uvme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_feedback);

        // editTextFeedbackName contains Name of sender

        EditText editTextFeedbackName = (EditText) findViewById(R.id.editTextFeedbackName);

        // editTextFeedbackDescription comprises the feedback text field

        EditText editTextFeedbackDescription = (EditText) findViewById(R.id.editTextFeedbackDescription);

        // button send feedback

        Button btn = (Button) findViewById(R.id.buttonSendFeedback);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/html");
                intent.putExtra(Intent.EXTRA_EMAIL, new String("umaid54321@hotmail.com"));
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
}