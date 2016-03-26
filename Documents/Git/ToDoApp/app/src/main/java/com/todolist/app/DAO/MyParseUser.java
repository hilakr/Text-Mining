package com.todolist.app.DAO;

import com.todolist.app.common.User;
import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created by Hila on 3/9/16.
 */
@ParseClassName("MyParseUser")
public class MyParseUser extends ParseUser {

    public void ParseUser() {};


    public void updateData(User user) {
        setIsManager(user.is_manager());
        setUserName(user.getUser_name());
        setUserEmail(user.getUser_email());
        setUserPassword(user.getUser_password());
        setUserPhone(user.getUser_phone());

    }

    public void setUserName(String userName) { put("userName", userName); }
    public void setUserEmail(String userEmail) { put("userEmail", userEmail); }
    public void setUserPassword(String userPassword) { put("userPassword", userPassword); }
    public void setUserPhone(String  userPhone) { put("userPhone", userPhone); }
    public void setIsManager(boolean isManager) { put("isManager", isManager); }

    public String getUserName(){ return getString("userName");}
    public String getUserEmail() { return getString("userEmail");}
    public String getUserPassword() {return getString ("userPassword");}
    public String  getUserPhone() {return getString("userPhone");}
    public boolean getIsManager() {return getBoolean("isManager");}

}
