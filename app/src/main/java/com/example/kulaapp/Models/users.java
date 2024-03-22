package com.example.kulaapp.Models;

public class users {
    int _id;
    String username, password, account_name, sync_datetime, branch;

    public users() {
        super();
        // TODO Auto-generated constructor stub
    }

    public users(int _id, String username, String password, String account_name, String sync_datetime, String branch) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.account_name = account_name;
        this.sync_datetime = sync_datetime;
        this.branch = branch;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getSync_datetime() {
        return sync_datetime;
    }

    public void setSync_datetime(String sync_datetime) {
        this.sync_datetime = sync_datetime;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
