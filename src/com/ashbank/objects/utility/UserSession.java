package com.ashbank.objects.utility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserSession {

    private static UserSession instance;
    private static final List<Notifications> notifications = new ArrayList<>();
    private static int unreadCount = 0;
    private String userID, username;

    /*=================== USER SESSION ===================*/

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

    /*=================== NOTIFICATIONS ===================*/

    public static void addNotification(String message) {
        notifications.add(new Notifications(message, LocalDateTime.now(), false));
        ++unreadCount;
    }

    public static List<Notifications> getNotifications() {
        return notifications;
    }

    public static int getUnreadCount() {
        return unreadCount;
    }

    public static void markAllAsRead() {
        for (Notifications notification : notifications) {
            notification.setReadStatus(true);
        }

        unreadCount = 0;
    }

    public static void clearNotifications() {
        notifications.clear();
        unreadCount = 0;
    }
}
