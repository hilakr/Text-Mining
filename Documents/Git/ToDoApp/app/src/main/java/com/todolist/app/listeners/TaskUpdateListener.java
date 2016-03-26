package com.todolist.app.listeners;


import com.todolist.app.common.Task;

import java.util.List;

public interface TaskUpdateListener {
    void onUpdate(List<Task> list, int code);
}
