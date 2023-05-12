package com.example.task1;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.task1.Utillities.SaveAndReadJson;
import com.example.task1.Utillities.SignalGenerator;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GameManager {
    private int crashes;
    private int currentPosition;
    private SignalGenerator signalGenerator;
    private SaveAndReadJson saveAndReadJson;
    private boolean makeNew;
    private int score;
    private int delay;
    private boolean sensors;
    private boolean isPaused;
    private int coinDelay ;

    public GameManager(int delay, boolean sensors) {
        this.delay = delay;
        this.sensors = sensors;
        this.crashes = 0;
        this.score = 0;
        this.currentPosition = 2;
        this.coinDelay = 2;
        this.isPaused = false;
        makeNew = true;
        signalGenerator = SignalGenerator.getInstance();
        saveAndReadJson = SaveAndReadJson.getInstance();
    }

    public int getCoinDelay() {
        return coinDelay;
    }

    public void setCoinDelay(int coinDelay) {
        this.coinDelay = coinDelay;
    }

    public boolean getPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public int getDelay() {
        return delay;
    }

    public boolean isSensors() {
        return sensors;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCrashes() {
        return crashes;
    }

    public void setCrashes(int crashes) {
        this.crashes = crashes;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public boolean isMakeNew() {
        return makeNew;
    }

    public void setMakeNew(boolean makeNew) {
        this.makeNew = makeNew;
    }

    public boolean getMakeNew() {
        return makeNew;
    }

    public int getRandom() {
        return new Random().nextInt(5);
    }

    public void vibrate() {
        signalGenerator.vibrate(500);
    }

    public void toast() {
        signalGenerator.toast("Crashed 🥺", 0);
    }

    public void saveRecord(Location location, List<Address> addresses) {
        if (saveAndReadJson.checkIfCanAddAndSave(score)) {
            saveAndReadJson.saveToJson(score, location.getLatitude(), location.getLongitude()
                    , addresses.get(0).getLocality(), addresses.get(0).getCountryName()
                    , (delay == 300) ? "HARD" : "EASY", (sensors) ? "TILT" : "BUTTONS");
        }
    }
}
