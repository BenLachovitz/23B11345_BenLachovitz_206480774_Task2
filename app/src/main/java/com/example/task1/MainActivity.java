package com.example.task1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.task1.Intarfaces.StepCallback;
import com.example.task1.Utillities.StepDetector;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final int ROWS = 8;
    private final int COLS = 5;
    private ShapeableImageView[][] main_IMG_rocks;
    private ShapeableImageView[][] main_IMG_coins;
    private ShapeableImageView[] main_IMG_cars;
    private ShapeableImageView[] main_IMG_hearts;
    private ExtendedFloatingActionButton main_FAB_left;
    private ExtendedFloatingActionButton main_FAB_right;
    private TextView main_TXT_score;
    private GameManager gameManager;
    private CountDownTimer timer;
    private MediaPlayer crashSound;
    private MediaPlayer coinCollectSound;
    private StepDetector stepDetector;
    private AppCompatImageButton main_BTN_pause;
    private ImageView main_IMG_blur;
    private Button main_BTN_mainMenu;
    private Button main_BTN_restart;
    private MaterialTextView main_TXT_pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent prevIntent = getIntent();
        Bundle extras = prevIntent.getExtras();
        gameManager = new GameManager(extras.getInt("DELAY"),extras.getBoolean("SENSOR"));
        findViews();
        initMechanism();
    }

    private void resumeGame() {
        main_IMG_blur.setVisibility(View.INVISIBLE);
        if(!gameManager.isSensors()) {
            main_FAB_left.setVisibility(View.VISIBLE);
            main_FAB_right.setVisibility(View.VISIBLE);
        }
        else
            stepDetector.start();
        main_BTN_pause.setImageResource(R.drawable.baseline_pause_24);
        main_BTN_mainMenu.setVisibility(View.INVISIBLE);
        main_BTN_restart.setVisibility(View.INVISIBLE);
        gameManager.setPaused(false);
        main_TXT_pause.setVisibility(View.INVISIBLE);
        startTime();
    }

    private void pauseGame() {
        timer.cancel();
        main_IMG_blur.setVisibility(View.VISIBLE);
        if(!gameManager.isSensors()) {
            main_FAB_left.setVisibility(View.INVISIBLE);
            main_FAB_right.setVisibility(View.INVISIBLE);
        }
        else
            stepDetector.stop();
        main_BTN_mainMenu.setVisibility(View.VISIBLE);
        main_BTN_pause.setImageResource(R.drawable.baseline_play_arrow_24);
        main_BTN_restart.setVisibility(View.VISIBLE);
        main_TXT_pause.setVisibility(View.VISIBLE);
        gameManager.setPaused(true);
    }

    private void startTimerAtFirstTimeDelay(){
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {

            public void run() {
                // Start Countdown timer after wait for 10 seconds
                startTime();
            }
        }, 500);
    }

    private void startTime()
    {
        long startTime= TimeUnit.MILLISECONDS.toMillis(3000);
        timer = new CountDownTimer(startTime,gameManager.getDelay()) {
            @Override
            public void onTick(long millisUntilFinished) {
                moveObs();
            }

            @Override
            public void onFinish() {
                restartTimer();
            }
        }.start();
    }
    private void restartTimer()
    {
        timer.cancel();
        startTime();
    }

    private void initMechanism()
    {
        if(!gameManager.isSensors())
            initViews();
        else
            initStepDetector();

        main_BTN_pause.setOnClickListener(v -> {
            if (!gameManager.getPaused()) {
                pauseGame();
            } else {
                resumeGame();
            }
        });
        main_BTN_mainMenu.setOnClickListener(v -> backToMenu());
        main_BTN_restart.setOnClickListener(v -> restartGame());
        crashSound = MediaPlayer.create(this, R.raw.crash);
        coinCollectSound = MediaPlayer.create(this,R.raw.coin_collect);
    }

    private void restartGame() {
        gameManager.setScore(0);
        for(int i=0 ; i<ROWS ;i++)
        {
            for(int j=0; j < COLS ; j++)
            {
                main_IMG_rocks[i][j].setVisibility(View.INVISIBLE);
                main_IMG_coins[i][j].setVisibility(View.INVISIBLE);
            }
        }
        for(int i=0;i<COLS;i++)
            main_IMG_cars[i].setVisibility(View.INVISIBLE);

        gameManager.setCrashes(0);
        gameManager.setCurrentPosition(COLS/2);
        gameManager.setMakeNew(true);
        main_IMG_cars[COLS/2].setVisibility(View.VISIBLE);
        gameManager.setCoinDelay(2);

        for(int i=0;i<3;i++)
            main_IMG_hearts[i].setVisibility(View.VISIBLE);

        resumeGame();
    }

    private void initViews()
    {
        main_FAB_left.setOnClickListener(v -> goLeft());
        main_FAB_right.setOnClickListener(v -> goRight());
    }

    private void initStepDetector() {
        main_FAB_left.setVisibility(View.GONE);
        main_FAB_right.setVisibility(View.GONE);
        stepDetector = new StepDetector(this, new StepCallback() {
            @Override
            public void stepLeft() {
                goLeft();
            }

            @Override
            public void stepRight() { goRight(); }

            @Override
            public void stepZ() {
                // pass
            }
        });
    }

    private void goLeft()
    {
        int tempPos = gameManager.getCurrentPosition();
        if(tempPos!=0) {
            main_IMG_cars[tempPos-1].setVisibility(View.VISIBLE);
            main_IMG_cars[tempPos--].setVisibility(View.INVISIBLE);
            gameManager.setCurrentPosition(tempPos);
            crashCheck();
            coinCollectCheck();
        }
    }

    private void goRight()
    {
        int tempPos = gameManager.getCurrentPosition();
        if(tempPos!=4) {
            main_IMG_cars[tempPos+1].setVisibility(View.VISIBLE);
            main_IMG_cars[tempPos++].setVisibility(View.INVISIBLE);
            gameManager.setCurrentPosition(tempPos);
            crashCheck();
            coinCollectCheck();
        }
    }

    private void crashCheck()
    {
        if(main_IMG_rocks[ROWS-1][gameManager.getCurrentPosition()].getVisibility()==View.VISIBLE) {
            main_IMG_rocks[ROWS-1][gameManager.getCurrentPosition()].setVisibility(View.INVISIBLE);
            crashSound.start();
            gameManager.setScore(gameManager.getScore() - 1);
            lifeLoss();
        }
        refreshScoreUI();
    }
    private void coinCollectCheck() {
        if (main_IMG_coins[ROWS-1][gameManager.getCurrentPosition()].getVisibility() == View.VISIBLE)
        {
            main_IMG_coins[ROWS-1][gameManager.getCurrentPosition()].setVisibility(View.INVISIBLE);
            coinCollectSound.start();
            gameManager.setScore(gameManager.getScore() + 10);
            refreshScoreUI();
        }
    }

    private void refreshScoreUI() {
        main_TXT_score.setText(gameManager.getScore()+"");
    }

    private void moveObs()
    {
        int randomRock = -1;
        for(int i=7;i>0;i--)
        {
            for(int j=4;j>=0;j--)
            {
                main_IMG_rocks[i][j].setVisibility(View.INVISIBLE);
                main_IMG_coins[i][j].setVisibility(View.INVISIBLE);
                if(main_IMG_rocks[i-1][j].getVisibility()==View.VISIBLE)
                {
                    main_IMG_rocks[i-1][j].setVisibility(View.INVISIBLE);
                    main_IMG_rocks[i][j].setVisibility(View.VISIBLE);
                }
                if(main_IMG_coins[i-1][j].getVisibility()==View.VISIBLE)
                {
                    main_IMG_coins[i-1][j].setVisibility(View.INVISIBLE);
                    main_IMG_coins[i][j].setVisibility(View.VISIBLE);
                }
            }
        }
        if(gameManager.getMakeNew())
        {
            gameManager.setMakeNew(false);
            randomRock = gameManager.getRandom();
            main_IMG_rocks[0][randomRock].setVisibility(View.VISIBLE);
        }
        else
            gameManager.setMakeNew(true);

        gameManager.setScore(gameManager.getScore() + 1);
        coinsGenerator(randomRock);
        crashCheck();
    }

    private void coinsGenerator(int randomRock)
    {
        int randomCoin;
        if(gameManager.getCoinDelay() == 0)
        {
            do {
                randomCoin = gameManager.getRandom();
            }while(randomCoin == randomRock);
            main_IMG_coins[0][randomCoin].setVisibility(View.VISIBLE);
            gameManager.setCoinDelay(gameManager.getRandom() + 3);
        }
        else
            gameManager.setCoinDelay(gameManager.getCoinDelay() - 1);
        coinCollectCheck();
    }

    private void lifeLoss()
    {
        int tempCrashes=gameManager.getCrashes();
        gameManager.toast();
        gameManager.vibrate();
        main_IMG_hearts[tempCrashes++].setVisibility(View.INVISIBLE);
        gameManager.setCrashes(tempCrashes);
        if(tempCrashes==3)
        {
            saveLocation(this);
            timer.cancel();
            backToMenu();
        }
    }

    @SuppressLint("MissingPermission")
    private void saveLocation(Context context)
    {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    gameManager.saveRecord(location,addresses);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
    private void backToMenu()
    {
        Intent mainMenuIntent = new Intent(this, MainMenu.class);
        startActivity(mainMenuIntent);
        finish();
    }

    private void findViews()
    {
        String temp;
        int resId;
        main_IMG_rocks = new ShapeableImageView[ROWS][COLS];
        for (int i = 1; i <= COLS; i++)
        {
            for (int j = 1; j <= ROWS; j++)
            {
                temp = "main_LINE"+i+"_rock"+j;
                resId = getResources().getIdentifier(temp,"id",getPackageName());
                main_IMG_rocks[j-1][i-1] = (ShapeableImageView) findViewById(resId);
            }
        }

        main_IMG_coins = new ShapeableImageView[ROWS][COLS];
        for (int i = 1; i <= COLS; i++)
        {
            for (int j = 1; j <= ROWS; j++)
            {
                temp = "main_LINE"+i+"_coin"+j;
                resId = getResources().getIdentifier(temp,"id",getPackageName());
                main_IMG_coins[j-1][i-1] = (ShapeableImageView) findViewById(resId);
            }
        }

        main_IMG_cars = new ShapeableImageView[] {findViewById(R.id.main_CAR_left),findViewById(R.id.main_CAR_midLeft),findViewById(R.id.main_CAR_center),findViewById(R.id.main_CAR_midRight),findViewById(R.id.main_CAR_right)};
        main_IMG_hearts = new ShapeableImageView[] {findViewById(R.id.main_IMG_heart1),findViewById(R.id.main_IMG_heart2),findViewById(R.id.main_IMG_heart3)};
        main_FAB_left = findViewById(R.id.main_BUTTON_left);
        main_FAB_right = findViewById(R.id.main_BUTTON_right);
        main_TXT_score = findViewById(R.id.main_TXT_score);
        Typeface customFont = Typeface.createFromAsset(getAssets(),"fonts/BillyOhio-85lM.ttf");
        main_TXT_score.setTypeface(customFont);
        main_BTN_pause = findViewById(R.id.main_BTN_pause);
        main_IMG_blur = findViewById(R.id.main_IMG_blur);
        main_BTN_mainMenu = findViewById(R.id.main_BTN_mainMenu);
        main_BTN_mainMenu.setTypeface(customFont);
        main_TXT_pause = findViewById(R.id.main_TXT_pause);
        main_TXT_pause.setTypeface(customFont);
        main_BTN_restart = findViewById(R.id.main_BTN_restart);
        main_BTN_restart.setTypeface(customFont);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!gameManager.getPaused()) {
            timer.cancel();
            if (gameManager.isSensors())
                stepDetector.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!gameManager.getPaused()) {
            if (gameManager.isSensors())
                stepDetector.start();
            startTimerAtFirstTimeDelay();
        }
    }
}