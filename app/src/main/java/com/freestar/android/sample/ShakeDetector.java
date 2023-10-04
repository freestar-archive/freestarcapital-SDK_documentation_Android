package com.freestar.android.sample;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static android.content.Context.SENSOR_SERVICE;

class ShakeDetector implements SensorEventListener {

    private static final float TRIGGER_THRESHOLD = 50f;
    private long lastTriggerTime, lastShakeTime;
    private SensorManager mSensorMgr;
    private Sensor accelerometer;
    private Activity activity;
    private static String TAG = "SHAKE_DETECTOR";
    private ShakeListener listener;

    private int count;

    public ShakeDetector(Activity activity, ShakeListener listener) {
        this.activity = activity;
        this.listener = listener;
        init();
        startListening();
    }

    private void init() {
        mSensorMgr = (SensorManager) activity.getSystemService(SENSOR_SERVICE);
        assert mSensorMgr != null;
        accelerometer = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private int triggerCounter;

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            long curTime = System.currentTimeMillis();

            if ((curTime - lastShakeTime) > 3000) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
                //Log.d(TAG, "Acceleration is " + acceleration + "m/s^2");
                if (acceleration > TRIGGER_THRESHOLD) {

                    Log.d(TAG, "triggered. diff since last trigger time: "  + (curTime - lastTriggerTime));

                    //took too long in between trigger events
                    if ((curTime - lastTriggerTime) > 2000) {
                        triggerCounter = 0; //so reset
                        lastTriggerTime = curTime; //still a trigger event
                        return; //but don't go further
                    }

                    lastTriggerTime = curTime; //within the previous trigger threshold of 2 seconds
                    triggerCounter++;

                    if (triggerCounter % 2 == 0) {

                        Log.d(TAG, "shaked. diff since last shake time: "  + (curTime - lastShakeTime));

                        if ((curTime - lastShakeTime) > 3000) {
                            listener.onShake();
                            lastShakeTime = curTime;
                            Log.d(TAG, "Shake, Rattle, and Roll.");
                            lastTriggerTime = curTime;
                        }
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
        mSensorMgr.unregisterListener(this);
        Log.d(TAG, "stopped");
    }

    public void startListening() {
        if (accelerometer != null) {
            mSensorMgr.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}