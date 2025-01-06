package com.ashbank.objects.people;

public class Employee extends Person{

    /*=================== DEFAULT DATA MEMBERS ===================*/
    private static final String DEFAULT_TEXT = "none";

    /*=================== DATA MEMBERS ===================*/
    private String employeeID, employeeQualification, employeeDepartment, employeePosition,
            dateEmployed;

    /**
     * Default constructor
     */
    public Employee() {
        super();
        this.employeeID = DEFAULT_TEXT;
        this.employeeQualification = DEFAULT_TEXT;
        this.employeeDepartment = DEFAULT_TEXT;
        this.employeePosition = DEFAULT_TEXT;
        this.dateEmployed = DEFAULT_TEXT;
    }

    /**
     * Employee Basic and Employment Data:
     * create employee object with bio and employment
     * data
     * @param lastName the last name of the employee
     * @param firstName the first name of the employee
     * @param gender the gender of the employee
     * @param birthDate the date of birth of the employee
     * @param age the age of the employee
     * @param employeeID the ID of the employee
     * @param position the position of the employee
     */
    public Employee(String employeeID, String lastName, String firstName, String gender, String birthDate, int age,
                    String dateEmployed, String employeeQualification, String employeeDepartment, String position) {
        super(lastName, firstName, gender, birthDate, age);
        this.employeeID = employeeID;
        this.employeeDepartment = employeeDepartment;
        this.employeeQualification = employeeQualification;
        this.employeePosition = position;
        this.dateEmployed = dateEmployed;
    }

    /**
     * Employee Object:
     * create a new employee object from an existing
     * employee object
     * @param employee the existing employee object
     */
    public Employee(Employee employee) {
        super(employee);
        this.employeeID = employee.getEmployeeID();
        this.employeeDepartment = employee.getEmployeeDepartment();
        this.employeeQualification = employee.getEmployeeQualification();
        this.employeePosition = employee.getEmployeePosition();
        this.dateEmployed = employee.getDateEmployed();
    }

    /*=================== SETTERS ===================*/

    public void setEmployeePosition(String position) {
        this.employeePosition = position;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public void setEmployeeQualification(String employeeQualification) {
        this.employeeQualification = employeeQualification;
    }

    public void setEmployeeDepartment(String employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
    }

    public void setDateEmployed(String dateEmployed) {
        this.dateEmployed = dateEmployed;
    }

    /*=================== GETTERS ===================*/

    public String getEmployeePosition() {
        return employeePosition;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getEmployeeQualification() {
        return employeeQualification;
    }

    public String getEmployeeDepartment() {
        return employeeDepartment;
    }

    public String getDateEmployed() {
        return dateEmployed;
    }

    /*=================== OTHER METHODS ===================*/
    public String toString() {
        return super.toString() + "\n" +
                "\nEmployment Information:\n" +
                "Qualification:\t" + this.getEmployeeQualification() + "\n" +
                "Department:\t\t" + this.getEmployeeDepartment() + "\n" +
                "Position:\t\t" + this.getEmployeePosition() + "\n" +
                "Date employed:\t" + this.getDateEmployed();
    }
}
