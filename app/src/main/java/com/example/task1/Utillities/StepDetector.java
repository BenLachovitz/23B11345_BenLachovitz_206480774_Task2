package com.example.task1.Utillities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.task1.Intarfaces.StepCallback;

public class StepDetector {
    private StepCallback stepCallback;
    private Sensor sensor;
    private SensorManager sensorManager;
    private long timeStamp = 0;
    private SensorEventListener sensorEventListener;

    public StepDetector(Context context, StepCallback stepCallback) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.stepCallback = stepCallback;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                calculateStep(x,y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void calculateStep(float x, float y) {
        if(System.currentTimeMillis() - timeStamp > 100)
        {
            timeStamp = System.currentTimeMillis();
            if(x >= 4.0) {
                if (stepCallback != null)
                    stepCallback.stepLeft();
            }
            if(x <= -4.0) {
                if (stepCallback != null)
                    stepCallback.stepRight();
            }
        }
    }

    public void start() {
        sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(sensorEventListener,sensor);
    }
}
