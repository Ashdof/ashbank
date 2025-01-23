package com.ashbank.objects.bank;

public class BankAccountTransactions {

    /*=================== DEFAULT DATA MEMBERS ===================*/
    private static final String DEFAULT_TEXT = "none";
    private static final Double DEFAULT_BALANCE = 0.00;

    /*=================== DATA MEMBERS ===================*/
    private String transactionID, accountID, transactionType, transactionDate, transactionDetails;
    private double transactionAmount;

    /**
     * Initialize Default Object:
     * create a new account transaction object with
     * default values
     */
    public BankAccountTransactions() {
        this.transactionID = DEFAULT_TEXT;
        this.accountID = DEFAULT_TEXT;
        this.transactionType = DEFAULT_TEXT;
        this.transactionAmount = DEFAULT_BALANCE;
        this.transactionDate = DEFAULT_TEXT;
        this.transactionDetails = DEFAULT_TEXT;
    }

    /**
     * Transaction Object:
     * create a new transaction object with the
     * given values
     * @param transactionID the id of the transaction
     * @param accountID the id of the account initiating the
     *                  transaction
     * @param transactionType the type of the transaction
     * @param transactionAmount the amount of the transaction
     * @param transactionDate the date of the transaction
     * @param transactionDetails the details of the transaction
     */
    public BankAccountTransactions(String transactionID, String accountID, String transactionType, double transactionAmount,
                                   String transactionDate, String transactionDetails) {
        this.transactionID = transactionID;
        this.accountID = accountID;
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.transactionDetails = transactionDetails;
    }

    /**
     * Transaction Object:
     * create a new transaction object from an existing
     * transaction object
     * @param bankAccountTransactions the existing transaction
     *                                object
     */
    public BankAccountTransactions(BankAccountTransactions bankAccountTransactions) {
        this.transactionID = bankAccountTransactions.getTransactionID();
        this.accountID = bankAccountTransactions.getAccountID();
        this.transactionType = bankAccountTransactions.getTransactionType();
        this.transactionAmount = bankAccountTransactions.getTransactionAmount();
        this.transactionDate = bankAccountTransactions.getTransactionDate();
        this.transactionDetails = bankAccountTransactions.getTransactionDetails();
    }

    public BankAccountTransactions(String accountID, String transactionType, double transactionAmount) {
        this.accountID = accountID;
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
    }

    /*=================== SETTERS ===================*/

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTransactionDetails(String transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    /*=================== GETTERS ===================*/

    public String getTransactionID() {
        return transactionID;
    }

    public String getAccountID() {
        return this.accountID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionDetails() {
        return transactionDetails;
    }

    /*=================== OTHER METHODS ===================*/

    @Override
    public String toString() {
        return "Transaction Summary\n" +
                "Account ID:\t\t" + this.getAccountID() + "\n" +
                "Transaction date:\t" + this.getTransactionDate() + "\n" +
                "Transaction type:\t" + this.getTransactionType() + "\n" +
                "Transaction amount:\t" + this.getTransactionAmount();
    }
}
