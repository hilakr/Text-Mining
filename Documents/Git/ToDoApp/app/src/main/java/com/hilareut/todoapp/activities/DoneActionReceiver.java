package com.hilareut.todoapp.activities;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hilareut.todoapp.DAO.DBHelper;
import com.hilareut.todoapp.common.Task;

/**
 * Created by Hila on 3/13/16.
 */
public class DoneActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(0);
        Task task =  (Task)intent.getSerializableExtra("task");

        DBHelper db = new DBHelper(context);
        task.setStatus("done");
        db.updateTask(task);


    }



}

