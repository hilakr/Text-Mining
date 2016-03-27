package com.hilareut.todoapp.activities;



        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;

        import com.hilareut.todoapp.R;
        import com.hilareut.todoapp.common.Task;

public class ReminderNotification extends BroadcastReceiver{

    public ReminderNotification() {
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        // The PendingIntent to launch our activity if the user selects this notification
        Task task = (Task)intent.getSerializableExtra("task");
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        // Set the info for the views that show in the notification panel.

        Intent snzInt = new Intent(context, SnoozeReminderReceiver.class);
        snzInt.putExtra("task", task);
        PendingIntent snoozeIntent = PendingIntent.getBroadcast(context, 0,snzInt, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent doneInt = new Intent(context,DoneActionReceiver.class);
        doneInt.putExtra("task", task);
        PendingIntent doneIntent = PendingIntent.getBroadcast(context, 0,doneInt, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Build notification
        Notification noti = new Notification.Builder(context)
                .setContentTitle("Time Reminder").setContentText(task.getDescription())
                .setSmallIcon(R.drawable.task_launcher).setContentIntent(contentIntent)
                .addAction(android.R.drawable.ic_lock_idle_alarm, "Snooze 1 hour", snoozeIntent)
                .addAction(R.drawable.ic_action_done, "Mark as done", doneIntent).setAutoCancel(true)
                .build();
        noti.defaults |= Notification.DEFAULT_SOUND;
        noti.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, noti);




    }

}
