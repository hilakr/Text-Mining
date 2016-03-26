package com.todolist.app.AlarmManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.todolist.app.DAO.DAO;
import com.todolist.app.DAO.DBHelper;

/**
 * Created by Hila on 3/15/16.
 */
public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    private static final int _REFRESH_INTERVAL = 60 * 1; // 1 minutes
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";
    public DAO dao ;

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        dao = DAO.getInstance(context);
        dao.loadFromParse();
    }

}