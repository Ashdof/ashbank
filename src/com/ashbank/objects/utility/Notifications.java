package com.ashbank.objects.utility;

import java.time.LocalDateTime;

public class Notifications {

    /*=================== DATA MEMBERS ===================*/
    private final String message;
    private final LocalDateTime localDateTime;
    private boolean readStatus;

    /*=================== DEFAULT VALUES ===================*/
    private static final String DEFAULT_TEXT_VALUE = "None";
    private static final boolean DEFAULT_STATUS = false;
    private static final LocalDateTime DEFAULT_DATE_TIME = LocalDateTime.now();


    /**
     * Default Constructor
     */
    public Notifications() {
        this.message = DEFAULT_TEXT_VALUE;
        this.localDateTime = DEFAULT_DATE_TIME;
        this.readStatus = DEFAULT_STATUS;

    }

    /**
     * Notifications:
     * a constructor that initializes a new notifications object
     * @param message the message
     * @param localDateTime the date and time
     * @param readStatus the read status of the message
     */
    public Notifications(String message, LocalDateTime localDateTime, boolean readStatus) {
        this.message = message;
        this.localDateTime = localDateTime;
        this.readStatus = readStatus;
    }

    /*=================== GETTERS ===================*/

    public String getMessage() {
        return message;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    /*=================== SETTERS ===================*/

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

}
