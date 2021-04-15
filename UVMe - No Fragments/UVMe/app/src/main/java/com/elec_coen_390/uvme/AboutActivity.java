package com.elec_coen_390.uvme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.elec_coen_390.uvme.R.layout.activity_about;

public class AboutActivity extends AppCompatActivity {

    ExpandableListViewAdapter listViewAdapter;
    ExpandableListView expandableListView;
    List<String> questionList;
    HashMap<String, List<String>> answerList;

    TextView textViewGithubURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(activity_about);

        expandableListView = findViewById(R.id.expandableListView);
        showList();
        setupURL();

        listViewAdapter = new ExpandableListViewAdapter(this, questionList, answerList);
        expandableListView.setAdapter(listViewAdapter);

        setupBottomNavigationListener();
    }




    private void showList() {

        questionList = new ArrayList<String>();
        answerList = new HashMap<String, List<String>>();

        questionList.add(getString(R.string.q_1));
        questionList.add(getString(R.string.q_2));
        questionList.add(getString(R.string.q_3));
        questionList.add(getString(R.string.q_4));
        questionList.add(getString(R.string.q_5));
        questionList.add(getString(R.string.q_6));
        questionList.add(getString(R.string.q_7));
        questionList.add(getString(R.string.q_8));
        questionList.add(getString(R.string.q_9));
        questionList.add(getString(R.string.q_10));
        questionList.add(getString(R.string.q_11));
        questionList.add(getString(R.string.q_12));
        questionList.add(getString(R.string.q_13));


       List <String> answer1 = new ArrayList<>();
        answer1.add(getString(R.string.ans_1a) +
             getString(R.string.ans_1b));


        List <String> answer2 = new ArrayList<>();
        answer2.add(getString(R.string.ans_2));

        List <String> answer3 = new ArrayList<>();
        answer3.add(getString(R.string.ans_3));

        List <String> answer4 = new ArrayList<>();
        answer4.add(getString(R.string.ans_4));


        List <String> answer5 = new ArrayList<>();
        answer5.add(getString(R.string.ans_5));

        List <String> answer6 = new ArrayList<>();
        answer6.add(getString(R.string.ans_6a) +
                getString(R.string.ans_6b) +
                getString(R.string.ans_6c));

        List <String> answer7 = new ArrayList<>();
        answer7.add(getString(R.string.ans_7a) +
                getString(R.string.ans_7b) +
                getString(R.string.ans_7c) +
                getString(R.string.ans_7d) +
                getString(R.string.ans_7e) +
                getString(R.string.ans_7f));

        List <String> answer8 = new ArrayList<>();
        answer8.add(getString(R.string.ans_8a) +
                getString(R.string.ans_8b));

        List <String> answer9 = new ArrayList<>();
        answer9.add(getString(R.string.ans_9a) +
                getString(R.string.ans_9b) +
                getString(R.string.ans_9c));

        List <String> answer10 = new ArrayList<>();
        answer10.add(getString(R.string.ans_10));

        List <String> answer11 = new ArrayList<>();
        answer11.add(getString(R.string.ans_11) +
                getString(R.string.ans_11b) +
                getString(R.string.ans_11c) +
                getString(R.string.ans_11d) +
                getString(R.string.ans_11e));

        List <String> answer12 = new ArrayList<>();
        answer12.add(getString(R.string.ans_12));

        List <String> answer13 = new ArrayList<>();
        answer13.add(getString(R.string.ans_13a) +
                getString(R.string.ans_13b) +
                getString(R.string.ans_13c) +
                getString(R.string.ans_13d) +
                getString(R.string.ans_13e) +
                getString(R.string.ans_13f));

        answerList.put(questionList.get(0), answer1);
        answerList.put(questionList.get(1), answer2);
        answerList.put(questionList.get(2), answer3);
        answerList.put(questionList.get(3), answer4);
        answerList.put(questionList.get(4), answer5);
        answerList.put(questionList.get(5), answer6);
        answerList.put(questionList.get(6), answer7);
        answerList.put(questionList.get(7), answer8);
        answerList.put(questionList.get(8), answer9);
        answerList.put(questionList.get(9), answer10);
        answerList.put(questionList.get(10), answer11);
        answerList.put(questionList.get(11), answer12);
        answerList.put(questionList.get(12), answer13);


    }

    private void setupURL() {
        textViewGithubURL = (TextView) findViewById(R.id.textViewAbout_AboutUVMe_GithubInfo);
        textViewGithubURL.setMovementMethod(LinkMovementMethod.getInstance());
    }

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
