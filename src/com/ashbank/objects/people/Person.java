package com.ashbank.objects.people;

public class Person{
    /*=================== DEFAULT DATA MEMBERS ===================*/
    private static final String DEFAULT_TEXT = "none";
    private static final int DEFAULT_AGE = 18;

    /*=================== DATA MEMBERS ===================*/
    private String lastName, firstName, gender, birthDate, postAddress, emailAddress, phoneNumber, photo;
    private int age;

    /***
     * Basic Bio Constructor:
     * Initialize the person object by providing the
     * basic bio data of the person
     * @param lastName the last name of the person
     * @param firstName the first name of the person
     * @param gender the gender of the person
     * @param age the age of the person
     */
    public Person(String lastName, String firstName, String gender, String birthDate, int age) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.age = age;
    }

    /***
     * Initialize Default Values:
     * create a new person with default values
     */
    public Person(){
        this.lastName = DEFAULT_TEXT;
        this.firstName = DEFAULT_TEXT;
        this.gender = DEFAULT_TEXT;
        this.birthDate =DEFAULT_TEXT;
        this.age = DEFAULT_AGE;
        this.postAddress = DEFAULT_TEXT;
        this.emailAddress = DEFAULT_TEXT;
        this.phoneNumber = DEFAULT_TEXT;
        this.photo = DEFAULT_TEXT;
    }

    /**
     * Person from Person:
     * create a new person from an existing
     * person object
     * @param person the existing Person object
     */
    public Person(Person person) {
        this.lastName = person.getLastName();
        this.firstName = person.getLastName();
        this.gender = person.getGender();
        this.birthDate = person.getBirthDate();
        this.age = person.getAge();
        this.postAddress = person.getPostAddress();
        this.emailAddress = person.getEmailAddress();
        this.phoneNumber = person.getPhoneNumber();
        this.photo = person.getPhoto();
    }

    /*=================== SETTERS ===================*/
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public  void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /*=================== GETTERS ===================*/
    public String getLastName() {
        return this.lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getGender() {
        return this.gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public int getAge() {
        return this.age;
    }

    public String getPostAddress() {
        return postAddress;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getPhoto() {
        return photo;
    }

    /*=================== OTHER METHODS ===================*/

    public String getFullName() {
        return this.getLastName() + ", " + this.getFirstName();
    }

    /**
     * @return a string representation of this person object
     */
    @Override
    public String toString() {

        return "Name:\t\t\t" + this.getFullName() + "\n" +
                "Gender:\t\t\t" + this.getGender() + "\n" +
                "Birth date:\t\t" + this.getBirthDate() + "\n" +
                "Age:\t\t\t" + this.getAge() + "\n" +
                "Postal address:\t" + this.getPostAddress() + "\n" +
                "Email address:\t" + this.getEmailAddress() + "\n" +
                "Phone number:\t" + this.getPhoneNumber() + "\n" +
                "Photo:\t\t\t" + this.getPhoto() + "\n";
    }

    /**
     * Compare Names:
     * compare the last and first names of two
     * Person objects
     * @param person the other Person object to
     *               compare with this Person
     *               object
     * @return 1 if this object is greater than the
     * other object, 0 if they are equal and -1 if
     * this object is smaller than the other object
     */
    public int compareNames(Person person) {
        int lastNameComparison = this.getLastName().compareToIgnoreCase(person.getLastName());

        if (lastNameComparison != 0)
            return lastNameComparison;

        return this.getFirstName().compareToIgnoreCase(person.getFirstName());
    }

    /**
     * Compare Ages:
     * compare two Person objects using
     * their ages
     * @param person the other object to
     *               compare with
     * @return 1 if this object' age is greater than the
     * other object's age, 0 if they are equal and -1 if
     * this object's age is smaller than the other object's
     * age
     */
    public int compareAges(Person person) {
        return Integer.compare(this.getAge(), person.getAge());
    }
}
