package com.hilareut.todoapp.activities;

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

import com.hilareut.todoapp.DAO.DAO;

import com.hilareut.todoapp.DAO.MyParseUser;
import com.hilareut.todoapp.R;
import com.hilareut.todoapp.common.User;
import com.hilareut.todoapp.dialogs.AboutDialog;
import com.hilareut.todoapp.dialogs.StudentsDialog;
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
        email.putExtra(Intent.EXTRA_SUBJECT, "Invitation to Join OTS team");
        email.putExtra(Intent.EXTRA_TEXT, "Hi\n" +
                "\tYou have been invited to be a team member in an OTS Team created by me.\n" +
                "\tUse this link to download and install the App from Google Play.\n" +
                "\t<LINK to Google Play download> ");
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
        finish();
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
                    finish();
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
