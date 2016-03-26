package com.todolist.app.activities;


import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.todolist.app.AlarmManager.MyAlarmReceiver;
import com.todolist.app.DAO.DAO;
import com.todolist.app.DAO.DBHelper;
import com.todolist.app.R;
import com.todolist.app.bl.SampleFragmentPagerAdapter;
import com.todolist.app.bl.TaskListFragmentAdapter;
import com.todolist.app.bl.UserListAdapter;
import com.todolist.app.common.Task;
import com.todolist.app.common.User;
import com.todolist.app.dialogs.AboutDialog;
import com.todolist.app.dialogs.StudentsDialog;
import com.todolist.app.dialogs.SyncDialog;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements
        ActionBar.TabListener {
    private ListView obj;
    DBHelper mydb;
    private  ViewPager viewPager;
    public   TaskListFragmentAdapter mWaitingTasksAdapter;
    public   TaskListFragmentAdapter mAllTasksAdapter;
    public    UserListAdapter mUsersListAdapter;
    private ActionBar actionBar;
    List<Task> all = Collections.synchronizedList(new ArrayList<Task>());
    List<Task> waiting = Collections.synchronizedList(new ArrayList<Task>());
    List<User> users = Collections.synchronizedList(new ArrayList<User>());
    DAO dao;
    //default 5 minutes
    int syncTime = 1;
    private Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      // Obtain the shared Tracker instance.
        App application = (App) getApplication();
        mTracker = application.getDefaultTracker();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        //set tabs to screen
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        dao = DAO.getInstance(getApplicationContext());
        dao.loadFromParse();
        mydb = new DBHelper(this.getApplicationContext());
        syncData(1);
        scheduleAlarm();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        if (ParseUser.getCurrentUser().getBoolean("isManager")){

            switch(item.getItemId())
            {
                case R.id.manageTeam:
                    Intent intent2 = new Intent(getApplicationContext(),UserActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.addNewTask:
                    Intent intent = new Intent(getApplicationContext(),DisplayTaskActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.app_name:
                    AboutDialog about = new AboutDialog();
                    about.show(getSupportFragmentManager(), "About");
                    return true;
                case R.id.owners:
                    StudentsDialog dialog = new StudentsDialog();
                    dialog.show(getSupportFragmentManager(), "Students");
                    return true;
                case R.id.changeTimeSync:
                    SyncDialog syncDialog = new SyncDialog();
                    syncDialog.show(getSupportFragmentManager(), "sync");
                    syncDialog.setDialogResult(new SyncDialog.NoticeDialogListener() {
                        public void finish(String result) {
                            // now you can use the 'result' on your activity
                            Log.e("mysync", result);
                            syncTime = Integer.valueOf(result);
                            scheduleAlarm();
                        }
                    });

                    return true;
                case R.id.logout:
                    ParseUser.logOut();
                    Intent intent3 = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent3);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }

        } else
        {
            switch(item.getItemId())
            {
                case R.id.addNewTask:
                    Intent intent = new Intent(getApplicationContext(),DisplayTaskActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.app_name:
                    AboutDialog about = new AboutDialog();
                    about.show(getSupportFragmentManager(), "About");
                    return true;
                case R.id.owners:
                    StudentsDialog dialog = new StudentsDialog();
                    dialog.show(getSupportFragmentManager(), "Students");
                    return true;
                case R.id.changeTimeSync:
                    SyncDialog syncDialog = new SyncDialog();
                    syncDialog.show(getSupportFragmentManager(), "sync");
                    syncDialog.setDialogResult(new SyncDialog.NoticeDialogListener() {
                        public void finish(String result) {
                            // now you can use the 'result' on your activity
                            Log.e("mysync", result);
                        }
                    });

                    return true;
                case R.id.logout:
                    ParseUser.logOut();
                    Intent intent3 = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent3);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }


    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
       // viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public void syncData (int i) {

        //Get current user
        ParseUser parseUser = ParseUser.getCurrentUser();
        User user = mydb.convertParseUsertoUser(parseUser);

        synchronized (waiting) {
           try{
               waiting = mydb.getWaitingTasks(user);
               waiting = sortList(waiting,i);
               //mWaitingTasksAdapter = new TaskListFragmentAdapter(mydb.getWaitingTasks(),this);
               mWaitingTasksAdapter = new TaskListFragmentAdapter(this, waiting);
               mWaitingTasksAdapter.notifyDataSetChanged();
           }
           finally {
               mydb.close();
           }
        }
        synchronized (all) {
           try{
               all = mydb.getAllTasks(user);
               all = sortList(all,i);
               //mWaitingTasksAdapter = new TaskListFragmentAdapter(mydb.getWaitingTasks(),this);
               mAllTasksAdapter = new TaskListFragmentAdapter(this, all);
               mAllTasksAdapter.notifyDataSetChanged();
           }
           finally {
               mydb.close();
           }
      }
        synchronized (users) {
            users = mydb.getAllUsers();
            mUsersListAdapter = new UserListAdapter(users, this);
            mUsersListAdapter.notifyDataSetChanged();
       }
    }

    //sort data
    public List<Task> sortList (List<Task> list,int i) {
        switch (i)
        {
            case 1:
                Collections.sort(list, new Comparator<Task>() {
                public int compare(Task o1, Task o2) {
                    if (o1.getDueDate() == null || o2.getDueDate() == null)
                        return 0;
                    return o1.getDueDate().compareTo(o2.getDueDate());
                }
            });
            case 2:
            Collections.sort(list, new Comparator<Task>() {
                public int compare(Task o1, Task o2) {
                    return o1.getPriority() > o2.getPriority() ? -1 : (o1.getPriority() > o2.getPriority() ) ? 1 : 0;

                }
            });
            return list;
            case 3:

                Collections.sort(list, new Comparator<Task>() {
                    public int compare(Task o1, Task o2) {
                        String status1 = o1.getStatus();
                        String status2 = o2.getStatus();
                        return status1.compareToIgnoreCase(status2);
                    }
                });
                return list;
            default:
                return list;
        }
    }

    // Setup a recurring alarm every half hour
    public void scheduleAlarm() {

        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        cancelAlarmIfExists(this,MyAlarmReceiver.REQUEST_CODE,intent);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                syncTime * 60000, pIntent);
        Log.e("scheduleAlarm", "sync");

    }



    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        SyncDialog dialog = new SyncDialog();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Screen Name", "Setting screen name: " + "Main Activity");
        mTracker.setScreenName("Main Activity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void cancelAlarmIfExists(Context mContext,int requestCode,Intent intent){
        try{
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent,0);
            AlarmManager am=(AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
            am.cancel(pendingIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}