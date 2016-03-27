package com.hilareut.todoapp.common;

/**
 * Created by Hila on 3/6/16.
 */
public class User {



    public User () {};
    public User(boolean is_manager, String  user_phone, String user_password, String user_email, String user_name, String user_id) {
        this.is_manager = is_manager;
        this.user_phone = user_phone;
        this.user_password = user_password;
        this.user_email = user_email;
        this.user_name = user_name;
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public boolean is_manager() {
        return is_manager;
    }

    public void setIs_manager(boolean is_manager) {
        this.is_manager = is_manager;
    }

    private String user_id;
    private String user_name;
    private String user_email;
    private String user_password;
    private String user_phone;
    private boolean is_manager;
    private String team;

}
