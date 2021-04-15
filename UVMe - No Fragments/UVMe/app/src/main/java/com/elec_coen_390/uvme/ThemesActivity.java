package com.elec_coen_390.uvme;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;


public class ThemesActivity extends AppCompatActivity {

    Button redButton;
    SharedPreferenceHelper sharedPreferenceHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        sharedPreferenceHelper = new SharedPreferenceHelper(this);
        getTheme().applyStyle(sharedPreferenceHelper.getTheme(),true);
        setContentView(R.layout.activity_themes);




        redButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sharedPreferenceHelper.saveRedTheme(R.style.OverlayThemeRed);
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        });
    }
    public class SharedPreferenceHelper {
        private SharedPreferences sharedPreferences;
        public SharedPreferenceHelper(Context context)
        {
            sharedPreferences = context.getSharedPreferences("ProfilePreference",
                    Context.MODE_PRIVATE );
        }
        public void saveRedTheme(int red){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("themeKey",red );
            editor.commit();
        }
        public int getTheme(){
            return sharedPreferences.getInt("themeKey", 0);
        }
        public void saveBlueTheme(int blue){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("themeKey",blue );
            editor.commit();
        }
    }
    public void setupUI(){
        redButton = findViewById(R.id.red);
    }

}
