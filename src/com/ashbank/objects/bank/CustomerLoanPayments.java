package com.ashbank.objects.bank;

public class CustomerLoanPayments {

    /*=================== DEFAULT DATA MEMBERS ===================*/
    private static final String DEFAULT_TEXT = "none";
    private static final Double DEFAULT_BALANCE = 0.00;

    /*=================== DATA MEMBERS ===================*/
    private String paymentID, loanID, paymentDate, paymentMethod, paymentDetails;
    private double paymentAmount;

    /**
     * Initialize Default Object:
     * create a payment object with default values
     */
    public CustomerLoanPayments() {
        this.paymentID = DEFAULT_TEXT;
        this.loanID = DEFAULT_TEXT;
        this.paymentDate = DEFAULT_TEXT;
        this.paymentMethod = DEFAULT_TEXT;
        this.paymentAmount = DEFAULT_BALANCE;
        this.paymentDetails = DEFAULT_TEXT;
    }

    /**
     * Loan Payment Object:
     * create a new loan payment object with the given
     * values
     * @param paymentID the id of the payment
     * @param loanID the id of the loan
     * @param paymentDate the date of the payment
     * @param paymentMethod the method of the payment
     * @param paymentAmount the amount of the payment
     * @param paymentDetails the details of the payment
     */
    public CustomerLoanPayments(String paymentID, String loanID, String paymentDate, String paymentMethod,
                                double paymentAmount, String paymentDetails) {
        this.paymentID = paymentID;
        this.loanID = loanID;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.paymentDetails = paymentDetails;
    }

    public CustomerLoanPayments(CustomerLoanPayments customerLoanPayments) {
        this.paymentID = customerLoanPayments.getPaymentID();
        this.loanID = customerLoanPayments.getLoanID();
        this.paymentDate = customerLoanPayments.getPaymentDate();
        this.paymentMethod = customerLoanPayments.getPaymentMethod();
        this.paymentAmount = customerLoanPayments.getPaymentAmount();
        this.paymentDetails = customerLoanPayments.getPaymentDetails();
    }

    /*=================== SETTERS ===================*/

    public void setLoanID(String loanID) {
        this.loanID = loanID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    /*=================== GETTERS ===================*/

    public String getLoanID() {
        return loanID;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    /*=================== OTHER METHODS ===================*/

    @Override
    public String toString() {
        return "Loan Payment Summary:\n" +
                "Loan:\t" + this.getLoanID() + "\n" +
                "Date:\t" + this.getPaymentDate() + "\n" +
                "Method:\t" + this.getPaymentMethod() + "\n" +
                "Amount:\tGHS" + this.getPaymentAmount() + "\n" +
                "Details:\t" + this.getPaymentDetails();
    }
}
