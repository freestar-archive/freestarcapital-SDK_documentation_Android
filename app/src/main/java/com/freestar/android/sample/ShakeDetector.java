package com.freestar.android.sample;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static android.content.Context.SENSOR_SERVICE;

class ShakeDetector implements SensorEventListener {

    private static String TAG = "SHAKE_DETECTOR";

    private static final float TRIGGER_THRESHOLD = 50f;
    private long lastTriggerTime, lastShakeTime;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private final Context context;
    private final ShakeListener listener;

    private int triggerCounter;

    private int count;

    public ShakeDetector(Context activity, ShakeListener listener) {
        this.context = activity;
        this.listener = listener;
        init();
        startListening();
    }

    private void init() {
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            long curTime = System.currentTimeMillis();

            //must wait at least 3 seconds to detect the next shake
            if ((curTime - lastShakeTime) > 3000) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
                //Log.d(TAG, "Acceleration is " + acceleration + "m/s^2");
                if (acceleration > TRIGGER_THRESHOLD) {

                    Log.d(TAG, "triggered. diff since last trigger time: " + (curTime - lastTriggerTime));

                    //happened too fast; was likely only one motion as sensor can be over-sensitive
                    // calibrate here
                    if ((curTime - lastTriggerTime) < 64) {
                        lastTriggerTime = curTime; //still a trigger event
                        return; //but don't go further
                    }

                    //valid trigger events must be within 2 seconds!
                    if ((curTime - lastTriggerTime) > 2000) {
                        triggerCounter = 0; //so reset
                        lastTriggerTime = curTime; //still a trigger event
                        return; //but don't go further
                    }

                    lastTriggerTime = curTime; //within the previous trigger threshold of 2 seconds
                    triggerCounter++;

                    if (triggerCounter % 2 == 0) {
                        Log.d(TAG, "shaked. diff since last shake time: " + (curTime - lastShakeTime));
                        listener.onShake();
                        lastShakeTime = curTime;
                        Log.d(TAG, "Shake, Rattle, and Roll.");
                        lastTriggerTime = curTime;
                        triggerCounter = 0;
                    }
                }
            } else {
                triggerCounter = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void stopListening() {
        sensorManager.unregisterListener(this);
        Log.d(TAG, "stopped");
    }

    public void startListening() {
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}