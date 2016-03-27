package com.hilareut.todoapp.activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hilareut.todoapp.common.Task;

import java.util.Calendar;

/**
 * Created by Hila on 3/13/16.
 */

public class SnoozeReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(0);
        Task task =  (Task)intent.getSerializableExtra("task");

        Intent myIntent = new Intent(context, ReminderNotification.class);
        myIntent.putExtra("task", task);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, Integer.valueOf(task.getId_task()), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.HOUR, 1);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }


}