package com.example.mess_mark_01.Model;

public class User {
    private String userName;
    private String phoneNumber;
    private String password;
    private String userID;
    private String userEmail;

    public User(String userName, String phoneNumber, String password) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public User(){}
    public User(String userName, String phoneNumber, String password,String emai) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.userEmail = emai;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getUserID(){
        return userID;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setUserID(String userID) {
        this.userID = userID;
    }
}
