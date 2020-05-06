package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        NotifHandler notifHandler = new NotifHandler(context);
        NotificationCompat.Builder nb = notifHandler.getChannelNotification("Alarm", "Wake Up");
        notifHandler.getManager().notify(1, nb.build());

        Intent alarmActivity = new Intent(context, AlarmActivity.class);
        context.startActivity(alarmActivity);
    }
}
