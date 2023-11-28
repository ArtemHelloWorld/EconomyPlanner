package com.example.economyplanner;

public class Item {

    Integer id;
    String name;
    Boolean status;
    String deadlineStart;
    String deadlineEnd;



    public Item(Integer id, String name, Boolean status, String deadlineStart, String deadlineEnd) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.deadlineStart = deadlineStart;
        this.deadlineEnd = deadlineEnd;

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
}