package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    final String TAG = "MainActivity";

    private Switch sw1,sw2, sw3;
    private EditText et2, et3;
    private TextView et1;
    private Button getBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sw1 = (Switch)findViewById(R.id.switch1);
        sw2 = (Switch)findViewById(R.id.switch2);
        sw3 = (Switch)findViewById(R.id.switch3);
        et1 = (TextView)findViewById(R.id.et1);
        et2 = (EditText)findViewById(R.id.et2);
        et3 = (EditText)findViewById(R.id.et3);
        getBtn = (Button)findViewById(R.id.getBtn);




        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.d(TAG, "Alarm on");
                    ConfigTimePicker();
                }
                else {
                    Log.d(TAG, "Alarm off");
                    ConfigCancelAlarm();
                }
            }
        });



        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
//                    shake service should run
                    Log.d(TAG, "Shake on");


                    String sensibilityToShake = et2.getText().toString();
                    try {
                        float number = Float.parseFloat(sensibilityToShake);
                        MotionDetector.setShakeThreshold(number);
                        Intent accelerationIntent = new Intent(MainActivity.this, MotionDetector.class);
                        startService(accelerationIntent);

                    } catch (NumberFormatException e) {
                       Log.d(TAG, "Not a float input");
                       sw2.setChecked(false);
                    }

                }
                else {
                    Log.d(TAG, "Shake off");
                    Intent accelerationIntent = new Intent(MainActivity.this, MotionDetector.class);
                    stopService(accelerationIntent);

                }
            }
        });

        sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
//                    shake service should run
                    Log.d(TAG, "Sleep on");


                    String degreeToSleep = et3.getText().toString();
                    try {
                        float number = Float.parseFloat(degreeToSleep);
                        MotionDetector.setShakeThreshold(number);
//                      todo Ali:
//                        run service

                    } catch (NumberFormatException e) {
                        Log.d(TAG, "Not a float input");
                        sw2.setChecked(false);
                    }

                }
                else {
                    Log.d(TAG, "Sleep off");
//                    todo Ali:
//                    stop service


                }
            }
        });







    }

    private void ConfigTimePicker() {
        DialogFragment timepicker = new TimePicker();
        timepicker.show(getSupportFragmentManager(), "time picker");
    }

    private void ConfigCancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        et1.setText("Alarm Canceled");
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        UpdateTimeTextView(c);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StartAlarm(c);
        }
    }

    private void UpdateTimeTextView(Calendar c) {
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        et1.setText("Alarm Set For: " + time);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void StartAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
}
