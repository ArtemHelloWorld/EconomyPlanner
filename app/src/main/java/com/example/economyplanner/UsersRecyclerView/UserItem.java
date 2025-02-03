package com.example.economyplanner.UsersRecyclerView;

public class UserItem {

    Integer id;
    String username;
    String jobTitle;

    public UserItem(Integer id, String username, String jobTitle) {
        this.id = id;
        this.username = username;
        this.jobTitle = jobTitle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
