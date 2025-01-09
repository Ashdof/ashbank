package com.ashbank.objects.utility;

public class UserSession {
    private static String userID, username;

    public UserSession(String userID, String username) {
        UserSession.userID = userID;
        UserSession.username = username;
    }

    public static void setUsername(String user) {
        UserSession.username = user;
    }

    public static void setUserID(String userID) {
        UserSession.userID = userID;
    }

    public static String getUsername() {
        return username;
    }

    public static String getUserID() {
        return userID;
    }

    public static void clearSession() {
        username = null;
    }
}
