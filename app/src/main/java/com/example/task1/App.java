package com.example.task1;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.example.task1.Utillities.MySP;
import com.example.task1.Utillities.SaveAndReadJson;
import com.example.task1.Utillities.SignalGenerator;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MySP.init(this);
        SignalGenerator.init(this);
        SaveAndReadJson.init();
    }
}
