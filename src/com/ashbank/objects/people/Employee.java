package com.ashbank.objects.people;

public class Employee extends Person{
    private String employeeID, employeePosition;

    /**
     * Employee Bio Data:
     * create user with bio data
     * @param lastName the last name of the employee
     * @param firstName the first name of the employee
     * @param gender the gender of the employee
     * @param birthDate the date of birth of the employee
     * @param age the age of the employee
     * @param employeeID the ID of the employee
     * @param position the position of the employee
     */
    public Employee(String employeeID, String lastName, String firstName, String gender, String birthDate, int age, String position) {
        super(lastName, firstName, gender, birthDate, age);
        this.employeeID = employeeID;
        this.employeePosition = position;
    }

    public Employee(Employee employee) {
        super(employee);
        this.employeeID = employee.getEmployeeID();
        this.employeePosition = employee.getEmployeePosition();
    }

    /*=================== SETTERS ===================*/

    public void setEmployeePosition(String position) {
        this.employeePosition = position;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    /*=================== GETTERS ===================*/

    public String getEmployeePosition() {
        return employeePosition;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    /*=================== OTHER METHODS ===================*/
    public String toString() {
        return super.toString() + "\n" +
                "Position:\t\t" + this.getEmployeePosition();
    }
}
