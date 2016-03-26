package com.todolist.app.AlarmManager;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.todolist.app.DAO.DAO;
import com.todolist.app.DAO.DBHelper;

/**
 * Created by Hila on 3/15/16.
 */

/*

A class within your application that extends IntentService and defines the onHandleIntent
which describes the work to do when this intent is executed:
 */

public class MyTestService extends IntentService {
    public MyTestService() {
        super("MyTestService");
    }
    public DAO dao;
    public DBHelper dbHelper;
    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.i("MyTestService", "Service running");

    }
}