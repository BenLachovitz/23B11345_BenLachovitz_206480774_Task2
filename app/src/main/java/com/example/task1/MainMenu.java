package com.example.task1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.task1.Utillities.SignalGenerator;
import com.google.android.material.button.MaterialButton;


public class MainMenu extends AppCompatActivity {
    private TextView menu_TXT_selectMode;
    private TextView menu_TXT_headline;
    private MaterialButton menu_BTN_easy;
    private MaterialButton menu_BTN_hard;
    private MaterialButton menu_BTN_score;
    private SignalGenerator signalGenerator;
    private ImageView menu_IMG_background;
    private CheckBox checkBox;
    private int delay;
    Typeface customFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        signalGenerator = SignalGenerator.getInstance();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainMenu.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},44);
        }
        customFont = Typeface.createFromAsset(getAssets(),"fonts/OsakaChips-K7d0D.ttf");
        findViews();
        initViews();
        Glide
                .with(this)
                .load(R.drawable.car_background)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(menu_IMG_background);
    }
    private void easyPress()
    {
        delay = 600;
        gameScreen();
    }
    private void hardPress()
    {
        delay = 300;
        gameScreen();
    }

    private void gameScreen()
    {
        Intent gameIntent = new Intent(signalGenerator.getContext(), MainActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("DELAY",delay);
        extras.putBoolean("SENSOR",checkBox.isChecked());
        gameIntent.putExtras(extras);
        startActivity(gameIntent);
    }

    private void scoreScreen()
    {
        Intent scoreIntent = new Intent(signalGenerator.getContext(), ScoreActivity.class);
        startActivity(scoreIntent);
    }

    private void initViews()
    {
        menu_BTN_easy.setOnClickListener(v -> easyPress());
        menu_BTN_hard.setOnClickListener(v -> hardPress());
        menu_BTN_score.setOnClickListener(v -> scoreScreen());
    }

    private void findViews()
    {
        checkBox = findViewById(R.id.menu_CHB_sensors);
        menu_IMG_background= findViewById(R.id.menu_IMG_background);
        menu_BTN_easy = findViewById(R.id.menu_BTN_easy);
        menu_BTN_hard = findViewById(R.id.menu_BTN_hard);
        menu_BTN_score = findViewById(R.id.menu_BTN_score);
        menu_TXT_selectMode = findViewById(R.id.menu_TXT_selectMode);
        menu_TXT_headline = findViewById(R.id.menu_TXT_headline);

        checkBox.setTypeface(customFont);
        menu_BTN_easy.setTypeface(customFont);
        menu_BTN_hard.setTypeface(customFont);
        menu_BTN_score.setTypeface(customFont);
        menu_TXT_headline.setTypeface(customFont);
        menu_TXT_selectMode.setTypeface(customFont);
    }
}