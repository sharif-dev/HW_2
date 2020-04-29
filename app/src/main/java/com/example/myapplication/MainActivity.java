package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    final String TAG = getClass().getSimpleName();

    private Switch sw1,sw2, sw3;
    private EditText et1, et2, et3;
    private Button getBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sw1 = (Switch)findViewById(R.id.switch1);
        sw2 = (Switch)findViewById(R.id.switch2);
        sw3 = (Switch)findViewById(R.id.switch3);
        et1 = (EditText)findViewById(R.id.et1);
        et2 = (EditText)findViewById(R.id.et2);
        et3 = (EditText)findViewById(R.id.et3);
        getBtn = (Button)findViewById(R.id.getBtn);




        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
//                alarm service should run
                    Log.d(TAG, "Alarm on");
                    String timeToAlarm = et1.getText().toString();
//                    todo sabrine:
//                    1. change string to time value
//                    2. if any error : make switch off & toast error
//                    3. else : turn on the alarm

                }
                else {
                    Log.d(TAG, "Alarm off");
//                    todo Sabrine:
//                    turn off the alarm
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


}
