package com.ashbank.objects.people;

import java.io.File;

public class Users extends Employees {

    /*=================== DEFAULT DATA MEMBERS ===================*/
    private static final String DEFAULT_TEXT = "none";

    /*=================== DATA MEMBERS ===================*/
    private String securityQuestion, securityAnswer, password, userID, username;

    public Users(String userID, String employeeID, String lastName, String firstName, String gender, String birthDate, int age, File photo, String qualification,
                 String dateEmployed, String department, String position, String securityQuestion, String securityAnswer, String username, String password) {
        super(employeeID, lastName, firstName, gender, birthDate, age, photo, dateEmployed, qualification, department, position);
        this.userID = userID;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.username = username;
        this.password = password;
    }

    public Users() {
        super();
        this.securityQuestion = DEFAULT_TEXT;
        this.securityAnswer = DEFAULT_TEXT;
        this.userID = DEFAULT_TEXT;
        this.username = DEFAULT_TEXT;
        this.password = DEFAULT_TEXT;
    }

    /**
     * Authenticate Employees:
     * authenticates users with login information
     *
     * @param users the users object
     */
    public Users(Users users) {
        super(users);
        this.userID = users.getUserID();
        this.username = users.getUsername();
        this.password = users.getPassword();
        this.securityQuestion = users.getSecurityQuestion();
        this.securityAnswer = users.getSecurityAnswer();
    }

    /*=================== SETTERS ===================*/

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*=================== GETTERS ===================*/

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    /*=================== OTHER METHODS ===================*/
    public String toString() {
        return super.toString() + "\n" +
                "\nPlatform Users Information:\n" +
                "Username:\t\t" + this.getUsername()  + "\n" +
                "Security question:\t" + this.getEmployeePosition();
    }
}
