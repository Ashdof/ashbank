package com.ashbank.objects.people;

public class User extends Employee {

    /*=================== DEFAULT DATA MEMBERS ===================*/
    private static final String DEFAULT_TEXT = "none";

    /*=================== DATA MEMBERS ===================*/
    private String securityQuestion, securityAnswer, password, userID, username;

    public User(String userID, String employeeID, String lastName, String firstName, String gender, String birthDate, int age, String qualification,
                String dateEmployed, String department, String position, String nationality, String nationalCard, String nationalCardNumber,
                String securityQuestion, String securityAnswer, String username, String password) {
        super(employeeID, lastName, firstName, gender, birthDate, age, dateEmployed, qualification, department, position, nationality, nationalCard, nationalCardNumber);
        this.userID = userID;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.username = username;
        this.password = password;
    }

    public User() {
        super();
        this.securityQuestion = DEFAULT_TEXT;
        this.securityAnswer = DEFAULT_TEXT;
        this.userID = DEFAULT_TEXT;
        this.username = DEFAULT_TEXT;
        this.password = DEFAULT_TEXT;
    }

    /**
     * Authenticate Employee:
     * authenticates user with login information
     *
     * @param user the user object
     */
    public User(User user) {
        super(user);
        this.userID = user.getUserID();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.securityQuestion = user.getSecurityQuestion();
        this.securityAnswer = user.getSecurityAnswer();
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
                "Username:\t\t" + this.getUsername()  + "\n" +
                "Security question:\t" + this.getEmployeePosition();
    }
}
