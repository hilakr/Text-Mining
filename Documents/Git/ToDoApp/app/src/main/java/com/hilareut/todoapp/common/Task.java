package com.hilareut.todoapp.common;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Hila on 2/21/16.
 */
public class Task implements Serializable {
    public Task(String id_task, String description, String category, String location, Date dueDate, String status, int accept, int priority, String assignedTo ,byte[] image ) {
        this.id_task = id_task;
        this.description = description;
        this.category = category;
        this.location = location;
        this.dueDate = dueDate;
        this.status = status;
        this.accept = accept;
        this.priority = priority;
        this.assignedTo = assignedTo;
        this.image = image;
    }

    public Task() {
        this.image = null;
    }

    public String getId_task() {
        return id_task;
    }

    public void setId_task(String id_task) {
        this.id_task = id_task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public boolean isHasDate() {
        return hasDate;
    }

    public void setHasDate(boolean hasDate) {
        this.hasDate = hasDate;
    }


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


    private String id_task;
    private String description;
    private String category;
    private String location;
    private Date dueDate;
    private String status;
    private int accept;
    private int priority;
    private String assignedTo;
    private boolean hasDate;
    private byte[] image = null;


}
