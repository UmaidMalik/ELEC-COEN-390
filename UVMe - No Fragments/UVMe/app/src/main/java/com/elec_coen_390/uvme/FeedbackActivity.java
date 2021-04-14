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
        setContentView(R.layout.activity_more);

        // edit 1 contains Name of sender

        EditText edit1 = (EditText)findViewById(R.id.edit1);

        // edit 2 comprises the feedback text field

        EditText edit2 = (EditText)findViewById(R.id.edit2);

        // button send feedback

        Button btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/html");
                i.putExtra(Intent.EXTRA_EMAIL, new String("umaid54321@hotmail.com"));
                i.putExtra(Intent.EXTRA_SUBJECT, "Feedback from UVMe App");
                i.putExtra(Intent.EXTRA_TEXT, "Sender:" +edit1.getText()+ "\n Message" +edit2.getText());
                try {
                    startActivity(Intent.createChooser(i,"Please select Eamil"));


                }
                catch (android.content.ActivityNotFoundException exception){

                    Toast.makeText(FeedbackActivity.this, "There are no Email Clients", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}