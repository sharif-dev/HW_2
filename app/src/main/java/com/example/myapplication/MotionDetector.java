package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MotionDetector extends Service implements SensorEventListener {

    final String TAG = getClass().getSimpleName();

    float xAccel, yAccel, zAccel;
    float xPreviousAccel, yPreviousAccel, zPreviousAccel;
    boolean firstUpdate = true;
    boolean shakeInitiated = false;
    Sensor accelerometer;
    SensorManager sm;
    private static float shakeThreshold = 4.5f;

    static public void setShakeThreshold(float shakeThreshold_) {
        shakeThreshold = shakeThreshold_;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "Service Started");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "on sensor changed called");


        updateAcceleratorParameters(event.values[0], event.values[1], event.values[2]);
        boolean isAccelerationDetected = isAccelerationDetected();
        Log.d(TAG, "is accelaration detected: " + isAccelerationDetected);

        if ((!shakeInitiated) && (isAccelerationDetected)) {
            shakeInitiated = true;
        } else if ((shakeInitiated) && (!isAccelerationDetected)) {
            shakeInitiated = false;
        } else if ((shakeInitiated) && (isAccelerationDetected)) {
            executeShakeAction();
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void executeShakeAction() {
//        Toast.makeText(getApplicationContext(), "Shake detected", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Shake detected");
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                PowerManager.FULL_WAKE_LOCK|
                PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "MyApp::MotionDetection");
//        todo
        wakeLock.acquire(1000);
        wakeLock.release();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Shake destroyed");
        sm.unregisterListener(this);

    }

    private boolean isAccelerationDetected() {
        int temp = 0;
        if (Math.abs(xPreviousAccel - xAccel) > shakeThreshold) temp += 1;
        if (Math.abs(yPreviousAccel - yAccel) > shakeThreshold) temp += 1;
        if (Math.abs(zPreviousAccel - zAccel) > shakeThreshold) temp += 1;
        Log.d(TAG, "temp value: " + temp);
        return temp > 1;
    }

    private void updateAcceleratorParameters(float xNewAccel, float yNewAccel, float zNewAccel) {
        if (firstUpdate) {
            xPreviousAccel = xNewAccel;
            yPreviousAccel = yNewAccel;
            zPreviousAccel = zNewAccel;
            firstUpdate = false;
        } else {
            xPreviousAccel = xAccel;
            yPreviousAccel = yAccel;
            zPreviousAccel = zAccel;
        }

        xAccel = xNewAccel;
        yAccel = yNewAccel;
        zAccel = zNewAccel;
    }
}
