package com.hilareut.todoapp.activities;

/**
 * Created by Hila on 3/6/16.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseUser;

/**
 * Created by rufflez on 7/8/14.
 */
public class DispatchActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Check if there is current user info
        if (ParseUser.getCurrentUser() != null) {
            // Start an intent for the logged in activity
            Intent intent = new Intent(DispatchActivity.this, MainActivity.class);
            boolean is_manager = ParseUser.getCurrentUser().getBoolean("isManager");
            intent.putExtra("is_manager", is_manager);
            startActivity(intent);
        } else {
            // Start and intent for the logged out activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
