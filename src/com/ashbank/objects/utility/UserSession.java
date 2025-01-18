package com.ashbank.objects.utility;

public class UserSession {

    private static UserSession instance;
    private String userID, username;

    private UserSession() {}

    public static UserSession getInstance() {

        if (instance == null)
            instance = new UserSession();

        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public String getUserID() {
        return userID;
    }

    public void clearSession() {
        username = null;
    }
}
