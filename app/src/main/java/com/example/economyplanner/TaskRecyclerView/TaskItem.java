package com.example.economyplanner.TaskRecyclerView;

public class TaskItem {

    Integer id;
    String name;
    Boolean status;
    String deadlineStart;
    String deadlineEnd;
    String timeCompleted;




    public TaskItem(Integer id, String name, Boolean status, String deadlineStart, String deadlineEnd, String timeCompleted) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.deadlineStart = deadlineStart;
        this.deadlineEnd = deadlineEnd;
        this.timeCompleted = timeCompleted;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDeadlineStart() {
        return deadlineStart;
    }

    public void setDeadlineStart(String deadlineStart) {
        this.deadlineStart = deadlineStart;
    }

    public String getDeadlineEnd() {
        return deadlineEnd;
    }

    public void setDeadlineEnd(String deadlineEnd) {
        this.deadlineEnd = deadlineEnd;
    }

    public String getTimeCompleted() {
        return timeCompleted;
    }

    public void setTimeCompleted(String timeCompleted) {
        this.timeCompleted = timeCompleted;
    }
}