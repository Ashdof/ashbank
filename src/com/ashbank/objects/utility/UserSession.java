package com.ashbank.objects.utility;

public class UserSession {
    private static String username;

    public static void setUsername(String user) {
        username = user;
    }

    public static String getUsername() {
        return username;
    }

    public static void clearSession() {
        username = null;
    }
}
