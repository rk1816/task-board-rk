package com.taskboard.models;

import java.util.List;
import java.util.ArrayList;

public class TaskList {
    private final int id;
    private final String name;
    private List<Task> tasks;

    public TaskList(int id, String name) {
        this.id = id;
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks != null ? new ArrayList<>(tasks) : new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    @Override
    public String toString() {
        return "TaskList{id=" + id + ", name='" + name + "', tasks=" + tasks + "}";
    }
}