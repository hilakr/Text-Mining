package com.hilareut.todoapp.listeners;


import com.hilareut.todoapp.common.Task;

import java.util.List;

public interface TaskUpdateListener {
    void onUpdate(List<Task> list, int code);
}
