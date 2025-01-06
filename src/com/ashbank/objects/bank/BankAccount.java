package com.ashbank.objects.bank;

public class BankAccount {

    /*=================== DEFAULT DATA MEMBERS ===================*/
    private static final String DEFAULT_TEXT = "none";
    private static final Double DEFAULT_BALANCE = 0.00;

    /*=================== DATA MEMBERS ===================*/
    private String accountID, customerID, accountNumber, accountType, accountCurrency, dateCreated,
            accountStatus, lastTransactionDate;
    private double accountBalance;

    /**
     * Initialize Default Object:
     * create a new bank account object with
     * default values
     */
    public BankAccount() {
        this.accountID = DEFAULT_TEXT;
        this.customerID = DEFAULT_TEXT;
        this.accountNumber = DEFAULT_TEXT;
        this.accountType = DEFAULT_TEXT;
        this.accountBalance = DEFAULT_BALANCE;
        this.accountCurrency = DEFAULT_TEXT;
        this.dateCreated = DEFAULT_TEXT;
        this.accountStatus = DEFAULT_TEXT;
        this.lastTransactionDate = DEFAULT_TEXT;

    }

    /**
     * Bank Account:
     * create a new bank account object with provided
     * values
     * @param accountID the id of the bank account
     * @param customerID the id of the customer
     * @param accountNumber the account number of the bank account
     * @param accountType the account type of the bank account
     * @param initialDeposit the initial deposit amount of the bank account
     * @param accountCurrency the default currency of the bank account
     * @param dateCreated the date the bank account was created
     * @param accountStatus the status of the bank account
     * @param lastTransactionDate the last transaction date of the bank account
     */
    public BankAccount(String accountID, String customerID, String accountNumber, String accountType, double initialDeposit, String accountCurrency,
                       String dateCreated, String accountStatus, String lastTransactionDate) {
        this.accountID = accountID;
        this.customerID = customerID;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountBalance = initialDeposit;
        this.accountCurrency = accountCurrency;
        this.dateCreated = dateCreated;
        this.accountStatus = accountStatus;
        this.lastTransactionDate = lastTransactionDate;
    }

    /**
     * Bank Account:
     * create a new bank account object from an existing
     * bank account object
     * @param bankAccount the existing bank account object
     */
    public BankAccount(BankAccount bankAccount) {
        this.accountID = bankAccount.getAccountID();
        this.customerID = bankAccount.getCustomerID();
        this.accountNumber = bankAccount.getAccountNumber();
        this.accountType = bankAccount.getAccountType();
        this.accountBalance = bankAccount.getAccountBalance();
        this.accountCurrency = bankAccount.getAccountCurrency();
        this.dateCreated = bankAccount.getDateCreated();
        this.accountStatus = bankAccount.getAccountStatus();
        this.lastTransactionDate = bankAccount.getLastTransactionDate();
    }

    /*=================== SETTERS ===================*/

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    /*=================== GETTERS ===================*/

    public String getAccountID() {
        return accountID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    @Override
    public String toString() {
        return "Account Information:\n" +
                "Date created:\t\t\t" + this.getDateCreated()  + "\n" +
                "Customer ID:\t\t\t" + this.getCustomerID()  + "\n" +
                "Account Type:\t\t\t" + this.getAccountType() + "\n" +
                "Account number:\t\t" + this.getAccountNumber()  + "\n" +
                "Account currency:\t\t" + this.getAccountCurrency() +  "\n" +
                "Last transaction date:\t" + this.getLastTransactionDate()  + "\n" +
                "Account status:\t\t\t" + this.getAccountStatus();
    }
}
