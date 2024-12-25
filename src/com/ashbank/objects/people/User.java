package com.ashbank.objects.people;

public class User extends Person{
    private String securityQuestion, securityAnswer, position, password, userID, username;

    /**
     * User Bio Data:
     * create user with bio data
     * @param lastName the last name of the user
     * @param firstName the first name of the user
     * @param gender the gender of the user
     * @param birthDate the date of birth of the user
     * @param age the age of the user
     * @param userID the ID of the user
     */
    public User(String lastName, String firstName, String gender, String birthDate, int age, String userID) {
        super(lastName, firstName, gender, birthDate, age);
        this.userID = userID;
    }

    public User(String userID, String lastName, String firstName, String gender, String birthDate, int age, String username, String securityQuestion, String securityAnswer, String position, String password) {
        super(lastName, firstName, gender, birthDate, age);
        this.userID = userID;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.position = position;
        this.username = username;
        this.password = password;
    }

    public User(User user) {
        super(user);
        this.userID = user.getUserID();
        this.securityQuestion = user.getSecurityQuestion();
        this.securityAnswer = user.getSecurityAnswer();
        this.position = user.getPosition();
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    /**
     * Authenticate User:
     * authenticates user with login information
     * @param username the username of the user
     * @param password the user's password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /*=================== SETTERS ===================*/

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    /*=================== GETTERS ===================*/

    public String getPassword() {
        return password;
    }

    public String getPosition() {
        return position;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getUsername() {
        return username;
    }

    public String getUserID() {
        return userID;
    }
}
