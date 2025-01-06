package com.ashbank.objects.bank;

public class CustomerLoanApplication {

    /*=================== DEFAULT DATA MEMBERS ===================*/
    private static final String DEFAULT_TEXT = "none";
    private static final Double DEFAULT_BALANCE = 0.00;
    private static final int DEFAULT_PERIOD = 0;

    /*=================== DATA MEMBERS ===================*/
    private String loanID, customerID, loanType, loanStartDate, loanEndDate, loanStatus;
    private double loanAmount, loanInterestRate;
    private int loanPeriod;

    /**
     * Initialize Default Object:
     * create a loan application object with
     * default values
     */
    public CustomerLoanApplication() {
        this.loanID = DEFAULT_TEXT;
        this.customerID = DEFAULT_TEXT;
        this.loanType = DEFAULT_TEXT;
        this.loanAmount = DEFAULT_BALANCE;
        this.loanInterestRate = DEFAULT_BALANCE;
        this.loanStartDate = DEFAULT_TEXT;
        this.loanEndDate = DEFAULT_TEXT;
        this.loanPeriod = DEFAULT_PERIOD;
        this.loanStatus = DEFAULT_TEXT;
    }

    /**
     * Loan Object:
     * create a new loan application object with the
     * provided values
     * @param loanID the id of the loan
     * @param customerID the id of the customer
     * @param loanType the type of the loan
     * @param loanAmount the amount of the loan
     * @param loanInterestRate the interest rate of the loan
     * @param loanStartDate the start date of the loan
     * @param loanEndDate the end date of the loan
     * @param loanPeriod the period of the loan
     * @param loanStatus the status of the loan
     */
    public CustomerLoanApplication(String loanID, String customerID, String loanType, double loanAmount, double loanInterestRate,
                                   String loanStartDate, String loanEndDate, int loanPeriod, String loanStatus) {
        this.loanID = loanID;
         this.customerID = customerID;
         this.loanType = loanType;
         this.loanAmount = loanAmount;
         this.loanInterestRate = loanInterestRate;
         this.loanStartDate = loanStartDate;
         this.loanEndDate = loanEndDate;
         this.loanPeriod = loanPeriod;
         this.loanStatus = loanStatus;
    }

    public CustomerLoanApplication(CustomerLoanApplication customerLoanApplication) {
        this.loanID = customerLoanApplication.getLoanID();
        this.customerID = customerLoanApplication.getCustomerID();
        this.loanType = customerLoanApplication.getLoanType();
        this.loanAmount = customerLoanApplication.getLoanAmount();
        this.loanInterestRate = customerLoanApplication.getLoanInterestRate();
        this.loanStartDate = customerLoanApplication.getLoanStartDate();
        this.loanEndDate = customerLoanApplication.getLoanEndDate();
        this.loanPeriod = customerLoanApplication.getLoanPeriod();
        this.loanStatus = customerLoanApplication.getLoanStatus();
    }

    /*=================== SETTERS ===================*/

    public void setLoanID(String loanID) {
        this.loanID = loanID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setLoanInterestRate(double loanInterestRate) {
        this.loanInterestRate = loanInterestRate;
    }

    public void setLoanStartDate(String loanStartDate) {
        this.loanStartDate = loanStartDate;
    }

    public void setLoanEndDate(String loanEndDate) {
        this.loanEndDate = loanEndDate;
    }

    public void setLoanPeriod(int loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    /*=================== GETTERS ===================*/

    public String getLoanID() {
        return loanID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getLoanType() {
        return loanType;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getLoanInterestRate() {
        return loanInterestRate;
    }

    public String getLoanStartDate() {
        return loanStartDate;
    }

    public String getLoanEndDate() {
        return loanEndDate;
    }

    public int getLoanPeriod() {
        return loanPeriod;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    /*=================== OTHER METHODS ===================*/

    @Override
    public String toString() {
        return "Loan Application Summary:\n" +
                "Customer ID:\t\t" + this.getCustomerID() + "\n" +
                "Type:\t\t" + this.getLoanType() + "\n" +
                "Amount:\t\tGHS" + this.getLoanAmount() + "\n" +
                "Interest rate:\t" + this.getLoanInterestRate() + "\n" +
                "Start date:\t" + this.getLoanStartDate() + "\n" +
                "End date:\t\t" + this.getLoanEndDate() + "\n" +
                "Period:\t\t" + this.getLoanPeriod() + "\n" +
                "Status:\t\t" + this.getLoanStatus();
    }
}
