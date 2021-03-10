package com.elec_coen_390.uvme;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import com.elec_coen_390.uvme.profileAtributes.eyeAdapter;
import com.elec_coen_390.uvme.profileAtributes.eyeColor;
import com.elec_coen_390.uvme.profileAtributes.skinAdapter;
import com.elec_coen_390.uvme.profileAtributes.skinTone;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ArrayList<eyeColor> meyeColor;
    private eyeAdapter meyeAdapter;
    private ArrayList<skinTone> mskinColor;
    private skinAdapter mskinAdapter;
    private TextView selectedProfileTextView;
    private EditText editTextPersonName;
    private EditText editTextAge;
    private Button editButton;
    private Button saveButton;
    private Button addProfileButton;
    private Spinner eyeColourSpinner;
    private Spinner skinToneSpinner;
    private Spinner genderSpinner;
    private SharedPreferences prefseye;
    private SharedPreferences prefsSkin;
    private SharedPreferences prefsGender;
    private final String prefNameEye = "spinner_value_eye";
    private final String prefNameSkin = "spinner_value_skin";
    private  final String prefNameGender ="spinner_value_gender";
    int id_gender,id_eye,id_skin=0;
    final String TAG = "ProfileActivity";







    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);
        setupBottomNavigationListener();
        // Setup Textviews
        saveButton = findViewById(R.id.saveButton);
        selectedProfileTextView=findViewById(R.id.selectedProfileTextView);
        noEdits(); // Default mode sends to NoEdits, Once edit is selected user can change informaiton
        Intent intent = getIntent(); // lets us go back and forth from app to app


    }
    @Override
    protected void onResume() {
        super.onResume();
        // this is where to save the button information
        SharedPreferences sharedPreferences = getSharedPreferences("profile-sharedPrefences", Context.MODE_PRIVATE); // open a fille
        String name =sharedPreferences.getString(getString(R.string.profileName),null);
        String age =sharedPreferences.getString(getString(R.string.profileAge),null);
        if (name == null) {
            loadData(); // if name is empty, load the data
        }
        else {
            // sets the name from user input and saves it in sharedPrefene files
            editTextPersonName.setText(name);
            editTextAge.setText(age);
            selectedProfileTextView.setText(name);
        }
    }
    private void setupBottomNavigationListener() {

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0); // bottom navigation menu index item {0(Profile),1(Home),2(More)}
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_home:
                        goToMainActivity();
                        break;

                    case R.id.action_profile:

                        break;

                    case R.id.action_more:
                        goToMoreActivity();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainActivity();
    }

    protected void goToMainActivity() {
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
        finish();
    }

    @SuppressLint("CutPasteId")
    protected void noEdits() {
        // function is used to display users information, edit mode is engaged when selected
        // setup
        editTextPersonName = findViewById(R.id.editTextPersonName);
        editTextAge = findViewById(R.id.editTextAge);
        editButton = findViewById(R.id.editButton);

        eyeColourSpinner = findViewById(R.id.eyeColourSpinner);
        skinToneSpinner = findViewById(R.id.skinToneSpinner);
        genderSpinner = findViewById(R.id.genderSpinner);

        // The following code is used to copy information from the Edit() function.
        // This displays the users selections on the spinners and loads all shared prefences files.
        // All edits are made in the Edit() function


        final Spinner genderSpin = (Spinner) findViewById(R.id.genderSpinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(ProfileActivity.this, R.array.gender, R.layout.color_spinner_layour);
        genderAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        genderSpin.setAdapter(genderAdapter);
        prefsGender = getSharedPreferences(prefNameGender, MODE_PRIVATE);
        id_gender = prefsGender.getInt("last_val_gender", 0);
        genderSpin.setSelection(id_gender); // saves the curser selection of the user.

        // On select function set to read users choice and sets the curser position for later use.(Using sharedPrefences)
        genderSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int Genderposition, long arg3) {
                prefsGender = getSharedPreferences(prefNameGender, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefsGender.edit();
                //save the values in the EditText view to preferences---
                editor.putInt("last_val_gender", Genderposition);
                //saves the values
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        initListEye(); // takes init information from the Icons set
        final Spinner eyeColor = (Spinner) findViewById(R.id.eyeColourSpinner);
        meyeAdapter = new eyeAdapter(this, meyeColor);
        eyeColor.setAdapter(meyeAdapter);

        // Share prefences information, finds position of the Spinner and saves in ID_eye
        prefseye = getSharedPreferences(prefNameEye, MODE_PRIVATE);
        id_eye = prefseye.getInt("last_val_eye", 0);
        eyeColor.setSelection(id_eye);

        // On select function set to read users choice and sets the curser position for later use.(Using sharedPrefences)
        eyeColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                prefseye = getSharedPreferences(prefNameEye, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefseye.edit();
                //save the values in the EditText view to preferences
                editor.putInt("last_val_eye", position);
                //saves the values
                editor.apply();
                eyeColor clickedItem = (eyeColor) arg0.getItemAtPosition(position);
                String clickedEye = clickedItem.getEyeColor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // Skin Tone DropDown setup information
        initListSkin(); // list used for user selection on set Icons for Skin tone ( Fitzpatric Scale )
        final Spinner skinTone = (Spinner) findViewById(R.id.skinToneSpinner);
        mskinAdapter = new skinAdapter(this, mskinColor);
        skinTone.setAdapter(mskinAdapter);

        // Share prefences information, finds position of the Spinner and saves in id_skin
        prefsSkin = getSharedPreferences(prefNameSkin, MODE_PRIVATE);
        id_skin = prefsSkin.getInt("last_val_skin", 0);
        skinTone.setSelection(id_skin);

        // On select function set to read users choice and sets the curser position for later use.(Using sharedPrefences)
        skinTone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                // TODO Auto-generated method stub
                prefsSkin = getSharedPreferences(prefNameSkin, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefsSkin.edit();
                //save the values in the EditText view to preferences
                editor.putInt("last_val_skin", pos);
                //saves the values
                editor.apply();
                skinTone clickedItem = (skinTone) arg0.getItemAtPosition(pos);
                String clickedSkin = clickedItem.getSkinColor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        enableModeNoEdit(); // Function used to set all enabled modes.
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Edits(); // sends to the edit function
                Toast.makeText(getApplicationContext(), "EDIT MODE", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // function is used for editing users information
    protected void Edits(){
        // setup
        editTextPersonName = findViewById(R.id.editTextPersonName);
        editTextAge = findViewById(R.id.editTextAge);
        editButton = findViewById(R.id.editButton);


        // spinner setup and setting the drop down icon to light blue
        eyeColourSpinner.getBackground().setColorFilter(getResources().getColor(R.color.font_lightblue), PorterDuff.Mode.SRC_ATOP);
        skinToneSpinner.getBackground().setColorFilter(getResources().getColor(R.color.font_lightblue), PorterDuff.Mode.SRC_ATOP);
        genderSpinner.getBackground().setColorFilter(getResources().getColor(R.color.font_lightblue), PorterDuff.Mode.SRC_ATOP);

        // Spinner Set up for Gender Selection.
        // Spinner uses XML files to format accordingly
        final Spinner genderSpin = (Spinner) findViewById(R.id.genderSpinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(ProfileActivity.this, R.array.gender, R.layout.color_spinner_layour);
        genderAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        genderSpin.setAdapter(genderAdapter);
        prefsGender = getSharedPreferences(prefNameGender, MODE_PRIVATE);
        id_gender = prefsGender.getInt("last_val_gender", 0);
        genderSpin.setSelection(id_gender); // saves the curser selection of the user.

        // On select function set to read users choice and sets the curser position for later use.(Using sharedPrefences)
        genderSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int Genderposition, long arg3) {
                prefsGender = getSharedPreferences(prefNameGender, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefsGender.edit();
                //save the values in the EditText view to preferences---
                editor.putInt("last_val_gender", Genderposition);
                //saves the values
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // The following code is used to set the spinners information in respect to the users selection.
        // The position is then saved, then called again in the NoEdit() function.
        initListEye(); // list of icons set by developers

        //Setup
        final  Spinner eyeColor=(Spinner) findViewById(R.id.eyeColourSpinner);
        meyeAdapter=new eyeAdapter(this,meyeColor);
        eyeColor.setAdapter(meyeAdapter);
        prefseye = getSharedPreferences(prefNameEye, MODE_PRIVATE);
        id_eye=prefseye.getInt("last_val_eye",0);
        eyeColor.setSelection(id_eye);

        eyeColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {
                prefseye = getSharedPreferences(prefNameEye, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefseye.edit();
                //save the values in the EditText view to preferences
                editor.putInt("last_val_eye", position);
                //saves the values
                editor.apply();
                eyeColor clickedItem = (eyeColor) arg0.getItemAtPosition(position); // gets position of the clicked item.
                String clickedEye=clickedItem.getEyeColor();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        initListSkin(); // lists of icons (Fitzpatrick Scale) designed by developers

        //setup
        final Spinner skinTone=(Spinner) findViewById(R.id.skinToneSpinner);
        mskinAdapter=new skinAdapter(this,mskinColor);
        skinTone.setAdapter(mskinAdapter);
        prefsSkin = getSharedPreferences(prefNameSkin, MODE_PRIVATE);
        id_skin=prefsSkin.getInt("last_val_skin",0);
        skinTone.setSelection(id_skin);

        skinTone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int pos, long arg3) {
                // TODO Auto-generated method stub
                prefsSkin = getSharedPreferences(prefNameSkin, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefsSkin.edit();
                //save the values in the EditText view to preferences
                editor.putInt("last_val_skin", pos);
                //saves the values
                editor.apply();
                skinTone clickedItem = (skinTone) arg0.getItemAtPosition(pos);
                String clickedSkin=clickedItem.getSkinColor();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        enableModeEdit(); // sets all enable modes.
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name =editTextPersonName.getText().toString();
                String profile = selectedProfileTextView.getText().toString();
                String age = editTextAge.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.mainFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.profileName),name);
                editor.putString(getString(R.string.profileAge),age);
                editor.putString("profile-sharedPrefences",profile);
                editor.apply(); // saved into the file
                selectedProfileTextView.setText(name); // shows the users edited profile name upon clicking edit
                noEdits(); // after user saved information it is sent back to the No edit mode.
                //selectedProfileTextView.setText(name);
                Toast.makeText(getApplicationContext(),"SAVED",Toast.LENGTH_LONG).show();
            }
        });
    }
    // override functions needed to output the Spinners information with their given locations.
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String text= adapterView.getItemAtPosition(position).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
    public void loadData() // takes the data from savebutton and loads it back into text box
    {
        SharedPreferences sharedPreferences = getSharedPreferences("profile-sharedPrefences",MODE_PRIVATE);
    }
    public void enableModeEdit(){
        editTextAge.setEnabled(true);
        editTextPersonName.setEnabled(true);
        saveButton.setEnabled(true);

        editButton.setEnabled(true);
        eyeColourSpinner.setEnabled(true);
        skinToneSpinner.setEnabled(true);
        genderSpinner.setEnabled(true);
    }
    public void enableModeNoEdit(){
        editTextAge.setEnabled(false);
        editTextPersonName.setEnabled(false);
        saveButton.setCursorVisible(true);
        saveButton.setEnabled(false);

        editButton.setEnabled(true);
        eyeColourSpinner.setEnabled(true);
        skinToneSpinner.setEnabled(false);
        genderSpinner.setEnabled(false);
    }
    private void initListEye(){
        meyeColor = new ArrayList<>();
        meyeColor.add(new eyeColor("Blue",R.drawable.ic_eye_blue));
        meyeColor.add(new eyeColor("Brown",R.drawable.ic_eye_brown));
        meyeColor.add(new eyeColor("Green",R.drawable.ic_eye_green));
        meyeColor.add(new eyeColor("Hazel",R.drawable.ic_eye_hazel));
    }
    private void initListSkin(){
        mskinColor = new ArrayList<>();
        mskinColor.add(new skinTone("Pale",R.drawable.ic_pale));
        mskinColor.add(new skinTone("Fair",R.drawable.ic_fair));
        mskinColor.add(new skinTone("Medium",R.drawable.ic_medium));
        mskinColor.add(new skinTone("Olive",R.drawable.ic_olive));
        mskinColor.add(new skinTone("Brown",R.drawable.ic_brown));
        mskinColor.add(new skinTone("Black",R.drawable.ic_black));
    }

    protected void goToMoreActivity() {
        Intent intentMore = new Intent(this, MoreActivity.class);
        startActivity(intentMore);
        finish();
    }



}
