package com.hilareut.todoapp.activities;

/**
 * Created by Hila on 3/6/16.
 */


import com.hilareut.todoapp.DAO.ParseTask;
import com.hilareut.todoapp.R;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(ParseTask.class);
        ParseObject.registerSubclass(ParseUser.class);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));
    }

    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
