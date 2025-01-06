package com.ashbank.objects.people;

public class Customer extends Person {

    /*=================== DEFAULT DATA MEMBERS ===================*/
    private static final String DEFAULT_TEXT = "none";

    /*=================== DATA MEMBERS ===================*/
    private String customerID, nextOfKinName, nextOfKinRelation, nextOfKinPhone, nextOfKinPostAddress,
            nextOfKinEmailAddress, beneficiaryName, beneficiaryRelation, beneficiaryPhone, beneficiaryPostAddress,
            beneficiaryEmailAddress,  profession, placeOfWork, position;

    /***
     * Initialize Default Values:
     * create a new customer object with default values
     */
    public Customer() {
        super();
        this.customerID = DEFAULT_TEXT;
        this.profession = DEFAULT_TEXT;
        this.placeOfWork = DEFAULT_TEXT;
        this.position = DEFAULT_TEXT;
        this.nextOfKinName = DEFAULT_TEXT;
        this.nextOfKinRelation = DEFAULT_TEXT;
        this.nextOfKinPhone = DEFAULT_TEXT;
        this.nextOfKinPostAddress = DEFAULT_TEXT;
        this.nextOfKinEmailAddress = DEFAULT_TEXT;
        this.beneficiaryName = DEFAULT_TEXT;
        this.beneficiaryRelation = DEFAULT_TEXT;
        this.beneficiaryPhone = DEFAULT_TEXT;
        this.beneficiaryPostAddress = DEFAULT_TEXT;
        this.beneficiaryEmailAddress = DEFAULT_TEXT;
    }

    /**
     * Customer Basic and Next of Kin Data:
     * create a customer object with basic and next of kin
     * data
     * @param customerID the id of the customer
     * @param lastName the last name of the customer
     * @param firstName the first name of the customer
     * @param gender the gender of the customer
     * @param birthDate the date of birth of the customer
     * @param age the age of the customer
     * @param nextOfKinName the full name of the customer's
     *                      next of kin
     * @param nextOfKinRelation the relation of the customer's
     *                          next of kin
     * @param nextOfKinPhone the phone number of the customer's
     *                       next of kin
     * @param nextOfKinPostAddress the postal address of the customer's
     *                             next of kin
     * @param nextOfKinEmailAddress the email address of the customer's
     *                              next of kin
     */
    public Customer(String customerID, String lastName, String firstName, String gender, String birthDate, int age,
                    String profession, String placeOfWork, String position, String nextOfKinName, String nextOfKinRelation,
                    String nextOfKinPhone, String nextOfKinPostAddress, String nextOfKinEmailAddress) {
        super(lastName, firstName, gender, birthDate, age);
        this.customerID = customerID;
        this.profession = profession;
        this.placeOfWork = placeOfWork;
        this.position = position;
        this.nextOfKinName = nextOfKinName;
        this.nextOfKinRelation = nextOfKinRelation;
        this.nextOfKinPhone = nextOfKinPhone;
        this.nextOfKinPostAddress = nextOfKinPostAddress;
        this.nextOfKinEmailAddress = nextOfKinEmailAddress;
    }

    /**
     * Customer Object:
     * create a new customer object from an existing
     * customer object
     * @param customer the existing customer object
     */
    public Customer(Customer customer) {
        super(customer);
        this.customerID = customer.getCustomerID();
        this.profession = customer.getProfession();
        this.placeOfWork = customer.getPlaceOfWork();
        this.position = customer.getPosition();
        this.nextOfKinName = customer.getNextOfKinName();
        this.nextOfKinRelation = customer.getNextOfKinRelation();
        this.nextOfKinPhone = customer.getNextOfKinPhone();
        this.nextOfKinPostAddress = customer.getNextOfKinPostAddress();
        this.nextOfKinEmailAddress = customer.getNextOfKinEmailAddress();
        this.beneficiaryName = customer.getBeneficiaryName();
        this.beneficiaryRelation = customer.getBeneficiaryRelation();
        this.beneficiaryPhone = customer.getBeneficiaryPhone();
        this.beneficiaryPostAddress = customer.getBeneficiaryPostAddress();
        this.beneficiaryEmailAddress = customer.getBeneficiaryEmailAddress();
    }

    /*=================== SETTERS ===================*/

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setNextOfKinName(String nextOfKinName) {
        this.nextOfKinName = nextOfKinName;
    }

    public void setNextOfKinRelation(String nextOfKinRelation) {
        this.nextOfKinRelation = nextOfKinRelation;
    }

    public void setNextOfKinPhone(String nextOfKinPhone) {
        this.nextOfKinPhone = nextOfKinPhone;
    }

    public void setNextOfKinPostAddress(String nextOfKinPostAddress) {
        this.nextOfKinPostAddress = nextOfKinPostAddress;
    }

    public void setNextOfKinEmailAddress(String nextOfKinEmailAddress) {
        this.nextOfKinEmailAddress = nextOfKinEmailAddress;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public void setBeneficiaryRelation(String beneficiaryRelation) {
        this.beneficiaryRelation = beneficiaryRelation;
    }

    public void setBeneficiaryPhone(String beneficiaryPhone) {
        this.beneficiaryPhone = beneficiaryPhone;
    }

    public void setBeneficiaryPostAddress(String beneficiaryPostAddress) {
        this.beneficiaryPostAddress = beneficiaryPostAddress;
    }

    public void setBeneficiaryEmailAddress(String beneficiaryEmailAddress) {
        this.beneficiaryEmailAddress = beneficiaryEmailAddress;
    }

    /*=================== GETTERS ===================*/

    public String getCustomerID() {
        return customerID;
    }

    public String getProfession() {
        return profession;
    }

    public String getPlaceOfWork() {
        return placeOfWork;
    }

    public String getPosition() {
        return position;
    }

    public String getNextOfKinName() {
        return nextOfKinName;
    }

    public String getNextOfKinRelation() {
        return nextOfKinRelation;
    }

    public String getNextOfKinPhone() {
        return nextOfKinPhone;
    }

    public String getNextOfKinPostAddress() {
        return nextOfKinPostAddress;
    }

    public String getNextOfKinEmailAddress() {
        return nextOfKinEmailAddress;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public String getBeneficiaryRelation() {
        return beneficiaryRelation;
    }

    public String getBeneficiaryPhone() {
        return beneficiaryPhone;
    }

    public String getBeneficiaryPostAddress() {
        return beneficiaryPostAddress;
    }

    public String getBeneficiaryEmailAddress() {
        return beneficiaryEmailAddress;
    }

    /*=================== OTHER METHODS ===================*/

    @Override
    public String toString() {
        return super.toString() + "\n" +
                "\nCareer Information:\n" +
                "Profession:\t\t" + this.getProfession()  + "\n" +
                "Place of work:\t" + this.getPlaceOfWork()  + "\n" +
                "Position:\t\t" + this.getPosition()  + "\n" +
                "\nNext of Kin Information:\n" +
                "Name:\t\t\t" + this.getNextOfKinName() + "\n" +
                "Relation:\t\t\t" + this.getNextOfKinRelation() + "\n" +
                "Phone number:\t" + this.getNextOfKinPhone() + "\n" +
                "Postal address:\t" + this.getNextOfKinPostAddress() + "\n" +
                "Email address:\t" + this.getNextOfKinEmailAddress() + "\n" +
                "\nBeneficiary Information:\n" +
                "Name:\t\t\t" + this.getBeneficiaryName() + "\n" +
                "Relation:\t\t" + this.getBeneficiaryRelation() + "\n" +
                "Phone number:\t" + this.getBeneficiaryPhone() + "\n" +
                "Postal address:\t" + this.getBeneficiaryPostAddress() + "\n" +
                "Email address:\t" + this.getBeneficiaryEmailAddress();
    }
}
