package com.example.task1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.task1.Intarfaces.CallBack_sendClick;
import com.example.task1.Models.Record;
import com.example.task1.Utillities.SignalGenerator;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class ScoreActivity extends AppCompatActivity {

    private TextView score_TXT_headline;
    private ListFragment fragmentList;
    private MapFragment mapsActivity;
    private SignalGenerator signalGenerator;
    private ImageView score_IMG_background;
    CallBack_sendClick callBack_sendClick = new CallBack_sendClick() {
        @Override
        public void userNameChosen(Record record) {
            showUserLocation(record);
        }
    };

    private void showUserLocation(Record record)
    {
        mapsActivity.changeMarker(record.getLat(),record.getLon());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        signalGenerator = SignalGenerator.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initFragments();
        beginTransactions();
        score_IMG_background = findViewById(R.id.score_IMG_background);
        Glide
                .with(this)
                .load(R.drawable.car_background)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(score_IMG_background);
        Typeface customFont = Typeface.createFromAsset(getAssets(),"fonts/OsakaChips-K7d0D.ttf");
        score_TXT_headline = findViewById(R.id.score_TXT_headline);
        score_TXT_headline.setTypeface(customFont);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack()
    {
        Intent mainMenuIntent = new Intent(signalGenerator.getContext(), MainMenu.class);
        startActivity(mainMenuIntent);
        finish();
    }

    private void initFragments()
    {
        fragmentList = new ListFragment();
        fragmentList.setCallback(callBack_sendClick);
        mapsActivity = new MapFragment();
    }

    private void beginTransactions()
    {
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRAME_list,fragmentList).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRAME_map, mapsActivity).commit();
    }
}