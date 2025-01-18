package com.ashbank.objects.people;

import java.io.File;

public class Person{

    /*=================== DEFAULT DATA MEMBERS ===================*/
    private static final String DEFAULT_TEXT = "none";
    private static final int DEFAULT_AGE = 18;

    /*=================== DATA MEMBERS ===================*/
    private String lastName, firstName, gender, birthDate, postAddress, emailAddress, phoneNumber, homePhoneNumber,
            nationality, nationalCardNumber, nationalCard, townOfResidence, suburbOfResidence,
            streetNameOfResidence, houseNumberOfResidence, gpsAddressOfResidence;
    File photo;
    private int age;

    /***
     * Initialize Default Values:
     * create a new person object with default values
     */
    public Person(){
        this.lastName = DEFAULT_TEXT;
        this.firstName = DEFAULT_TEXT;
        this.gender = DEFAULT_TEXT;
        this.birthDate =DEFAULT_TEXT;
        this.age = DEFAULT_AGE;
        this.nationality = DEFAULT_TEXT;
        this.nationalCard = DEFAULT_TEXT;
        this.nationalCardNumber = DEFAULT_TEXT;
        this.townOfResidence = DEFAULT_TEXT;
        this.suburbOfResidence = DEFAULT_TEXT;
        this.streetNameOfResidence = DEFAULT_TEXT;
        this.houseNumberOfResidence = DEFAULT_TEXT;
        this.gpsAddressOfResidence = DEFAULT_TEXT;
        this.postAddress = DEFAULT_TEXT;
        this.emailAddress = DEFAULT_TEXT;
        this.phoneNumber = DEFAULT_TEXT;
        this.homePhoneNumber = DEFAULT_TEXT;
        this.photo = null;
    }

    /***
     * Basic Bio Constructor:
     * Initialize the person object by providing the
     * basic bio data of the person
     * @param lastName the last name of the person
     * @param firstName the first name of the person
     * @param gender the gender of the person
     * @param age the age of the person
     */
    public Person(String lastName, String firstName, String gender, String birthDate, int age, File photo) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.age = age;
        this.photo = photo;
    }

    public Person(String lastName, String firstName, String gender, String birthDate, int age, File photo,
                  String nationality, String nationalCard, String nationalCardNumber, String townOfResidence,
                  String suburbOfResidence, String streetNameOfResidence, String houseNumberOfResidence,
                  String gpsAddressOfResidence, String postAddress, String emailAddress, String phoneNumber,
                  String homePhoneNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.age = age;
        this.photo = photo;
        this.nationality = nationality;
        this.nationalCard = nationalCard;
        this.nationalCardNumber = nationalCardNumber;
        this.townOfResidence = townOfResidence;
        this.suburbOfResidence = suburbOfResidence;
        this.streetNameOfResidence = streetNameOfResidence;
        this.houseNumberOfResidence = houseNumberOfResidence;
        this.gpsAddressOfResidence = gpsAddressOfResidence;
        this.postAddress = postAddress;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.homePhoneNumber = homePhoneNumber;
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
        this.nationality = person.getNationality();
        this.nationalCard = person.getNationalCard();
        this.nationalCardNumber = person.getNationalCardNumber();
        this.townOfResidence = person.getTownOfResidence();
        this.suburbOfResidence = person.getSuburbOfResidence();
        this.streetNameOfResidence = person.getStreetNameOfResidence();
        this.houseNumberOfResidence = person.getHouseNumberOfResidence();
        this.gpsAddressOfResidence = person.getGpsAddressOfResidence();
        this.postAddress = person.getPostAddress();
        this.emailAddress = person.getEmailAddress();
        this.phoneNumber = person.getPhoneNumber();
        this.homePhoneNumber = person.getHomePhoneNumber();
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

    public void setPhoto(File photo) {
        this.photo = photo;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setNationalCard(String nationalCard) {
        this.nationalCard = nationalCard;
    }

    public void setNationalCardNumber(String nationalCardNumber) {
        this.nationalCardNumber = nationalCardNumber;
    }

    public void setTownOfResidence(String townOfResidence) {
        this.townOfResidence = townOfResidence;
    }

    public void setSuburbOfResidence(String suburbOfResidence) {
        this.suburbOfResidence = suburbOfResidence;
    }

    public void setStreetNameOfResidence(String streetNameOfResidence) {
        this.streetNameOfResidence = streetNameOfResidence;
    }

    public void setHouseNumberOfResidence(String houseNumberOfResidence) {
        this.houseNumberOfResidence = houseNumberOfResidence;
    }

    public void setGpsAddressOfResidence(String gpsAddressOfResidence) {
        this.gpsAddressOfResidence = gpsAddressOfResidence;
    }

    public void setHomePhoneNumber(String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
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

    public File getPhoto() {
        return photo;
    }

    public String getNationality() {
        return nationality;
    }

    public String getNationalCard() {
        return nationalCard;
    }

    public String getNationalCardNumber() {
        return nationalCardNumber;
    }

    public String getTownOfResidence() {
        return townOfResidence;
    }

    public String getSuburbOfResidence() {
        return suburbOfResidence;
    }

    public String getStreetNameOfResidence() {
        return streetNameOfResidence;
    }

    public String getHouseNumberOfResidence() {
        return houseNumberOfResidence;
    }

    public String getGpsAddressOfResidence() {
        return gpsAddressOfResidence;
    }

    public String getHomePhoneNumber() {
        return homePhoneNumber;
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

        return "Basic Information:\n" +
                "Name:\t\t\t" + this.getFullName() + "\n" +
                "Gender:\t\t\t" + this.getGender() + "\n" +
                "Birth date:\t\t" + this.getBirthDate() + "\n" +
                "Age:\t\t\t" + this.getAge() + "\n" +
                "Photo:\t\t\t" + this.getPhoto() + "\n" +
                "\nNational Identity Information:\n" +
                "Nationality:\t\t" + this.getNationality()  + "\n" +
                "National card:\t" + this.getNationalCard()  + "\n" +
                "Card number:\t\t" + this.getNationalCardNumber()  + "\n" +
                "\nAddress and Contact Information:\n" +
                "Postal address:\t" + this.getPostAddress() + "\n" +
                "Email address:\t" + this.getEmailAddress() + "\n" +
                "Phone number:\t" + this.getPhoneNumber()  + "\n" +
                "Home phone:\t" + this.getHomePhoneNumber();
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
