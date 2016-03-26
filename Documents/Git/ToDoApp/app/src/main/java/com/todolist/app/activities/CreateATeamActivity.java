package com.todolist.app.activities;

/**
 * Created by Hila on 3/6/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.todolist.app.DAO.DAO;

import com.todolist.app.DAO.MyParseUser;
import com.todolist.app.R;
import com.todolist.app.common.User;
import com.todolist.app.dialogs.AboutDialog;
import com.todolist.app.dialogs.StudentsDialog;
import com.parse.ParseUser;

public class CreateATeamActivity extends AppCompatActivity {

    private DAO dao;
    private MyParseUser parseUser;
    private EditText eMailto;
    private EditText eEmailAddress;
    private EditText eUserPhone;
    private Button bSendMailButton;
    private Button bContinueToApp;

    public CreateATeamActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_a_team_activity);

        eMailto = (EditText)findViewById(R.id.editMailto);
        eEmailAddress = (EditText)findViewById(R.id.editEmailAddress);
        eUserPhone = (EditText)findViewById(R.id.editUserPhone);
        bSendMailButton = (Button)findViewById(R.id.sendMailButton);
        bContinueToApp = (Button)findViewById(R.id.continueButton);
        dao = DAO.getInstance(getApplicationContext());

//    // Set up the submit button click handler
//    findViewById(R.id.sendMailButton).setOnClickListener(new View.OnClickListener() {
//        public void onClick(View view) {
//
//            String mailto,emailAddress, userPhone;
//            mailto = eMailto.getText().toString();
//            emailAddress = eEmailAddress.getText().toString();
//            userPhone = eUserPhone.getText().toString();
//            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//            emailIntent.setData(Uri.parse(mailto + emailAddress));
//            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Invitation to Join OTS team");
//            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, You have been invited to be team member in an OTS Team created by me. Use this line to downliad and inatall the App from Google Play. Your default password is your phone number.");
//            try {
//                startActivity(Intent.createChooser(emailIntent, "Send email using..."));
//            } catch (android.content.ActivityNotFoundException ex) {
//                Toast.makeText(getApplicationContext(), "No email clients installed.", Toast.LENGTH_SHORT).show();
//            }
//        }}
//
//
//    );

    }


    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }


    public void sendMailToUserButton(View button) {
        //Manager invite team members to App
        //user phone is the password of the user.

        String mailto, emailAddress, userPhone;
        mailto = eMailto.getText().toString();
        emailAddress = eEmailAddress.getText().toString();
        userPhone = eUserPhone.getText().toString();
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        email.putExtra(Intent.EXTRA_SUBJECT, "subject");
        email.putExtra(Intent.EXTRA_TEXT, "message");
        email.setType("message/rfc822");
        try {
            startActivity(Intent.createChooser(email, "Choose an Email client :"));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
        //CREATE USER AND SAVE IN PARSE
        //TODO SAVE USER IN LOCAL SERVER
        User user = new User();
        user.setUser_name(mailto);
        user.setUser_email(emailAddress);
        user.setUser_password(userPhone);
        user.setUser_phone(userPhone);
        user.setIs_manager(false);
        dao.addUser(user);
    }

    public void continueToAppButton(View bContinueToApp) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("EXTRA_SESSION_ID", true);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_team, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

            switch (item.getItemId()) {
                case R.id.mangeTask:
                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.app_name:
                    AboutDialog about = new AboutDialog();
                    about.show(getSupportFragmentManager(), "About");
                    return true;
                case R.id.owners:
                    StudentsDialog dialog = new StudentsDialog();
                    dialog.show(getSupportFragmentManager(), "Students");
                    return true;
                case R.id.logout:
                    ParseUser.logOut();
                    Intent intent3 = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent3);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,UserActivity.class);
        //Start the activity
        startActivity(intent);
        finish();
    }

}
