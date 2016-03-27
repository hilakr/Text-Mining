package com.hilareut.todoapp.DAO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.hilareut.todoapp.common.Task;
import com.hilareut.todoapp.common.User;
import com.parse.ParseUser;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "new1531.db";
    public static final String CONTACTS_TABLE_NAME = "tasks";
    public static final String CONTACTS_COLUMN_TASK_ID = "_id";
    public static final String CONTACTS_COLUMN_TASK_NAME = "taskName";
    public static final String CONTACTS_COLUMN_TASK_CATEGORY = "category";
    public static final String CONTACTS_COLUMN_TASK_LOCATION = "location";
    public static final String CONTACTS_COLUMN_TASK_DUE_DATE = "dueDate";
    public static final String CONTACTS_COLUMN_TASK_STATUS = "status";
    public static final String CONTACTS_COLUMN_TASK_ACCEPT = "accept";
    public static final String CONTACTS_COLUMN_TASK_ASSIGNED_TO = "assignedTo";
    public static final String CONTACTS_COLUMN_TASK_PRIORITY = "priority";
    public static final String CONTACTS_COLUMN_TASK_IMAGE = "image";
    public static final String USERS_TABLE_NAME = "users";
    public static final String USER_ID = "_id";
    public static final String USER_NAME = "username";
    public static final String USER_EMAIL = "email";
    public static final String USER_PHONE = "phone";
    public static final String USER_PASSWORD = "password";
    public static final String USER_IS_MANAGER = "isManager";

    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table tasks " +
                        "(_id text primary key, taskName text,category text,location text, dueDate text,status text, accept integer, priority integer, assignedTo text, image text)"
        );
        db.execSQL(
                "create table users " +
                        "(_id text primary key, username text,password text,email text, phone text,isManager text)"
        );

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS tasks");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean insertTask  (Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", task.getId_task());
        contentValues.put("taskName", task.getDescription());
        contentValues.put("category", task.getCategory());
        contentValues.put("location", task.getLocation());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date date = task.getDueDate();
        if (date != null)
        {
            contentValues.put("dueDate", sdf.format(task.getDueDate()));

        }else
        {
            contentValues.put("dueDate", sdf.format(new Date()));

        }

        contentValues.put("status", task.getStatus());
        contentValues.put("accept", task.getAccept());
        contentValues.put("priority", task.getPriority());
        contentValues.put("assignedTo", task.getAssignedTo());
        contentValues.put("image", task.getImage());
        return db.insert("tasks", null, contentValues) >= 0;
    }

    public boolean insertUser  (User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", user.getUser_id());
        contentValues.put("username", user.getUser_name());
        contentValues.put("email", user.getUser_email());
        contentValues.put("phone", user.getUser_phone());
        contentValues.put("isManager",user.is_manager());
        return db.insert("users", null, contentValues) >= 0;
    }

    public Cursor getData(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.query("tasks", null, "_id=?", new String[] {id}, null,null,null);
            if (res.moveToNext())
            {
                return res;
            }
       return null;
    }

    public Cursor getDataForUser(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.query("users", null, "_id=?", new String[] {id}, null,null,null);
        if (res.moveToNext())
        {
            return res;
        }
        return null;
    }

    public boolean updateTask (Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("taskName", task.getDescription());
        contentValues.put("category", task.getCategory());
        contentValues.put("location", task.getLocation());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date date = task.getDueDate();
        if (date != null)
        {
            contentValues.put("dueDate", sdf.format(task.getDueDate()));

        }else
        {
            contentValues.put("dueDate", sdf.format(new Date()));

        }
        contentValues.put("status", task.getStatus());
        contentValues.put("accept",task.getAccept());
        contentValues.put("priority", task.getPriority());
        contentValues.put("assignedTo", task.getAssignedTo());
        contentValues.put("image", task.getImage());
        db.update("tasks", contentValues, "_id = ? ", new String[] {task.getId_task()} );
        return true;
    }


    public Integer deleteTask (String id_task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("tasks",
                "_id = ? ",
                new String[] { id_task });
    }

    public ArrayList getAllTasks(User user)
    {

        Cursor res;
        ArrayList array_list = new ArrayList<Task>();
        SQLiteDatabase db = this.getReadableDatabase();;
        if (user.is_manager())
        {
             res =  db.rawQuery( "select * from tasks", null );
        }
        else
        {
            String name = user.getUser_name();
            res =  db.rawQuery( "select * from tasks where assignedTo ='" + name +"'",null );
        }

        res.moveToFirst();
        while(res.isAfterLast() == false){
            Task task = new Task();
            task.setId_task(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TASK_ID)));
            task.setDescription(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TASK_NAME)));
            String date = res.getString(res.getColumnIndex(CONTACTS_COLUMN_TASK_DUE_DATE));
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            try {
                Date myDate = dateFormat.parse(date);
                System.out.println(date);
                task.setDueDate(myDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            task.setCategory(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TASK_CATEGORY)));
            task.setLocation(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TASK_LOCATION)));
            task.setPriority(res.getInt(res.getColumnIndex(CONTACTS_COLUMN_TASK_PRIORITY)));
            task.setStatus(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TASK_STATUS)));
            task.setAccept(res.getInt(res.getColumnIndex(CONTACTS_COLUMN_TASK_ACCEPT)));
            task.setAssignedTo(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TASK_ASSIGNED_TO)));
            try{
                  byte[] bytes = res.getBlob(res.getColumnIndex(CONTACTS_COLUMN_TASK_IMAGE));
                  task.setImage(bytes);
            }
            catch (Exception e)
            {
                  e.printStackTrace();
            }



            array_list.add(task);
            res.moveToNext();
        }
        return array_list;
    }


    public ArrayList getAllUsers()
    {
        ArrayList array_list = new ArrayList<User>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from users", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            User user = new User();
            user.setUser_id(res.getString(res.getColumnIndex(USER_ID)));
            user.setUser_email(res.getString(res.getColumnIndex(USER_EMAIL)));
            user.setUser_phone(res.getString(res.getColumnIndex(USER_PHONE)));
            user.setIs_manager(Boolean.valueOf(res.getString(res.getColumnIndex(USER_IS_MANAGER))));
            user.setUser_name(res.getString(res.getColumnIndex(USER_NAME)));
            array_list.add(user);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getAllUsersNames()
    {
        ArrayList array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from users", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String name = res.getString(res.getColumnIndex(USER_NAME));
            array_list.add(name);
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList getWaitingTasks(User user){

        Cursor res;
        ArrayList waiting_list = new ArrayList<Task>();
        SQLiteDatabase db = this.getReadableDatabase();
        String waiting = "waiting";
        if (user.is_manager())
        {
            res =  db.rawQuery( "select * from tasks where status ='" + waiting +"'", null );
        }
        else
        {
            String name = user.getUser_name();
            res =  db.rawQuery( "select * from tasks where assignedTo ='" + name +"' and status ='" + waiting +"'",null );
        }
        res.moveToFirst();
        while(res.isAfterLast() == false){
               Task task = new Task();
               task.setId_task(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TASK_ID)));
               task.setDescription(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TASK_NAME)));
               String date = res.getString(res.getColumnIndex(CONTACTS_COLUMN_TASK_DUE_DATE));
               SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
               try {
                   Date myDate = dateFormat.parse(date);
                   System.out.println(date);
                   task.setDueDate(myDate);
               } catch (ParseException e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
               }
               task.setCategory(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TASK_CATEGORY)));
               task.setLocation(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TASK_LOCATION)));
               task.setPriority(res.getInt(res.getColumnIndex(CONTACTS_COLUMN_TASK_PRIORITY)));
               task.setStatus(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TASK_STATUS)));
               task.setAccept(res.getInt(res.getColumnIndex(CONTACTS_COLUMN_TASK_ACCEPT)));
               task.setAssignedTo(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TASK_ASSIGNED_TO)));
               task.setImage(res.getBlob(res.getColumnIndex(CONTACTS_COLUMN_TASK_IMAGE)));
               waiting_list.add(task);
               res.moveToNext();
        }
      return waiting_list;
    }


    public User convertParseUsertoUser(ParseUser parseUser){
        //Create local user
        User user = new User();
        user.setUser_name(parseUser.getUsername());
        String phone = parseUser.get("phone").toString();
        user.setUser_phone(phone);
        user.setIs_manager(Boolean.valueOf(parseUser.get("isManager").toString()));
        user.setUser_email(parseUser.getEmail());
        return user;
    }

}