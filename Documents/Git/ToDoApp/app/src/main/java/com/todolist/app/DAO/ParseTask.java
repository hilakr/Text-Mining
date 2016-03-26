package com.todolist.app.DAO;

import com.todolist.app.common.Task;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("ParseTask")
public class ParseTask extends ParseObject {

    public void ParseTask() {};

    public void updateData(Task task) {
        setDescription(task.getDescription());
        setCategory(task.getCategory());
        setLocation(task.getLocation());
        setPriority(task.getPriority());
        setDueDate(task.getDueDate().toString());
        setAccept(task.getAccept());
        setStatus(task.getStatus());
        setAssignedTo(task.getAssignedTo());
        if (task.getImage() != null) {
            setImage(task.getImage());
        }


    }

    public void setDescription(String description) { put("taskName", description); }
    public void setCategory(String category) { put("category", category); }
    public void setLocation(String location) { put("location", location); }
    public void setDueDate(String dueDate) { put("dueDate", dueDate); }
    public void setPriority(int priority) { put("priority", priority); }
    public void setStatus(String status) { put("status", status); }
    public void setAccept(int accept) { put("accept", accept); }
    public void setAssignedTo(String assignedTo) { put("assignedTo", assignedTo); }
    public void setImage(byte[] image) { put("image", image); }


    public String getDescription() { return getString("taskName"); }
    public String getCategory() { return getString("category"); }
    public String getLocation() { return getString("location"); }
    public int getPriority() { return getInt("priority"); }
    public String getDueDate() { return getString("dueDate"); }
    public int getAccept() {return  getInt("accept");}
    public String getStatus() { return getString("status"); }
    public String getAssignedTo() { return getString("assignedTo"); }
    public byte[] getImage() { return getBytes("image"); }




}
