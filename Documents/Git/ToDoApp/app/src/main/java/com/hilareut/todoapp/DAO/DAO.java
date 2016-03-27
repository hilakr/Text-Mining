package com.hilareut.todoapp.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hilareut.todoapp.common.Task;
import com.hilareut.todoapp.common.User;
import com.hilareut.todoapp.listeners.LoginListener;
import com.hilareut.todoapp.listeners.TaskUpdateListener;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hila on 3/6/16.
 */
public class DAO {

    private static DAO instance;
    private Context context;
    private ArrayList<TaskUpdateListener> listeners = new ArrayList<TaskUpdateListener>();
    private LoginListener loginListener;
    private DBHelper mydb;

    private DAO(Context context) {
        this.context = context;
    }

    public static DAO getInstance(Context applicationContext) {
        if (instance == null)
            instance = new DAO(applicationContext);
        return instance;
    }


    public int updateTask(final Task task) {
        //UPDATE TASK
        ParseQuery<ParseTask> query = new ParseQuery<ParseTask>(ParseTask.class);
        query.getInBackground(task.getId_task(), new GetCallback<ParseTask>() {
            @Override
            public void done(ParseTask parseTask, ParseException e) {
                if (e == null) {
                    parseTask.updateData(task);
                    parseTask.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            try{
                                mydb = new DBHelper(context);
                                if (mydb.updateTask(task)) {
                                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                                     //Intent intent = new Intent(context, MainActivity.class);
                                    //startActivity(intent);
                                } else {
                                    Toast.makeText(context, "not Updated", Toast.LENGTH_SHORT).show();
                                }
                            }finally {
                                mydb.close();
                            }

                        }
                    });
                }

            }
        });
        return 1;
    }


    public void loadFromParse() {
        ParseQuery<ParseTask> query = new ParseQuery<ParseTask>(ParseTask.class);
      //query.orderByDescending(getOrderByColumnString(orderbyColumn));
            query.findInBackground(new FindCallback<ParseTask>() {
                public void done(List<ParseTask> taskList, ParseException e) {
                    if (e == null) {
                        try {
                            DBHelper mydb = new DBHelper(context);
                            ArrayList<Task> tasks = new ArrayList<>();
                            for (int i = 0; i < taskList.size(); i++) {
                                Task myTask = new Task();
                                ParseTask parsetask = (taskList.get(i));
                                myTask.setId_task(parsetask.getObjectId());
                                myTask.setDescription(parsetask.getDescription());
                                myTask.setCategory(parsetask.getCategory());
                                myTask.setLocation(parsetask.getLocation());
                                String date = parsetask.getDueDate();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
                                try {
                                    Date myDate = dateFormat.parse(date);
                                    System.out.println(date);
                                    myTask.setDueDate(myDate);
                                } catch (java.text.ParseException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                                myTask.setPriority(parsetask.getPriority());
                                myTask.setStatus(parsetask.getStatus());
                                myTask.setAccept(parsetask.getAccept());
                                myTask.setAssignedTo(parsetask.getAssignedTo());
                                myTask.setImage(parsetask.getImage());
                                mydb.insertTask(myTask);
                                tasks.add(myTask);
                            }
                        }
                        finally {
                            System.out.print("wtf");
                        }
                    } else {
                        Log.d("getfromParse", "error");
                        e.printStackTrace();
                    }
                }
            });


            ParseQuery<ParseUser> userQuery = new ParseQuery<ParseUser>(ParseUser.class);
            userQuery.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> usersList, ParseException e) {
                    if (e == null) {
                        try {
                            DBHelper mydb = new DBHelper(context);
                            ArrayList<User> users = new ArrayList<>();
                            for (int i = 0; i < usersList.size(); i++) {
                                User myUser = new User();
                                ParseUser parseUser = (usersList.get(i));
                                myUser.setUser_id(parseUser.getObjectId());
                                myUser.setUser_name(parseUser.getUsername());
                                myUser.setUser_email(parseUser.getEmail());
                                myUser.setUser_phone(parseUser.getString("phone"));
                                myUser.setIs_manager(parseUser.getBoolean("isManager"));
                                myUser.setUser_password(parseUser.getString("phone"));
                                mydb.insertUser(myUser);
                                users.add(myUser);
                            }
                        }
                            finally {
                            System.out.print("wtf");
                        }

                    } else {
                        Log.d("getfromParse", "error");
                        e.printStackTrace();
                    }
                }
            });
        }


    public void addUser(final User myUser) {
        // Set up a new Parse user
        ParseUser user = new ParseUser();
        user.setUsername(myUser.getUser_name());
        user.setPassword(myUser.getUser_password());
        user.setEmail(myUser.getUser_email());
        // other fields can be set just like with ParseObject
        user.put("phone", myUser.getUser_phone());
        user.put("isManager", myUser.is_manager());
        user.put("firstlogin",0);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    try {
                        //DBHelper mydb = new DBHelper(context);
                        //mydb.insertUser(myUser);
                    }
                    finally {
                       // mydb.close();
                    }
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.e("signup",e.getLocalizedMessage());
                }
            }
        });


    }


}


