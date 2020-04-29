package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

public class AlarmActivity extends AppCompatActivity {

    private Gyroscope gyroscope;
    private CountDownTimer musicCountDownTimer;
    public static final String TAG = "AlarmActivity";
    private MediaPlayer mediaPlayer;
    private Float rotationSpeed = 3.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        ConfigAlarmSound();
        ConfigGyroscope();
    }

    private void ConfigAlarmSound() {
        Uri alarmSound = RingtoneManager. getDefaultUri (RingtoneManager.TYPE_ALARM );
        mediaPlayer = MediaPlayer. create (getApplicationContext(), alarmSound);

        musicCountDownTimer = new CountDownTimer(60 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "onTick: music starts"+ millisUntilFinished);
                mediaPlayer.start();
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: music finished");
                mediaPlayer.stop();
            }
        }.start();

    }

    private void ConfigGyroscope() {
        gyroscope = new Gyroscope(this);
        gyroscope.SetListener(new Gyroscope.Listener() {
            @Override
            public void OnRotation(float rx, float ry, float rz) {
                if (rz > rotationSpeed || rz < -rotationSpeed){
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                    mediaPlayer.stop();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        gyroscope.Register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gyroscope.UnRegister();
    }
}
