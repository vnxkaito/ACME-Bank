package com.acme.entities;

import com.acme.services.account.AccountService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Account extends AccountService {
    private String accountId;
    private String userId;
    private double balance;
    private String accountType;
    private String cardType;
    private boolean isLocked;

    public Account(){

    }

    public static void main(String[] args) throws IOException {
        Account account = new Account();
        account.setAccountId("c1ch");
        account.setAccountType("Checking");
        account.setBalance(1000);
        account.setCardType("Mastercard Titanium");
        account.setUserId("customer1");

        account.create(account);

        account = new Account();
        account.setAccountId("c1sv");
        account.setAccountType("Saving");
        account.setBalance(1000);
        account.setCardType("Mastercard Titanium");
        account.setUserId("customer1");

        account.create(account);

        account = new Account();
        account.setAccountId("c2ch");
        account.setAccountType("Checking");
        account.setBalance(1000);
        account.setCardType("Mastercard Platinum");
        account.setUserId("customer2");

        account.create(account);

        account = new Account();
        account.setAccountId("c2sv");
        account.setAccountType("Saving");
        account.setBalance(1000);
        account.setCardType("Mastercard Platinum");
        account.setUserId("customer2");

        account.create(account);

        account = new Account();
        account.setAccountId("c3ch");
        account.setAccountType("Checking");
        account.setBalance(1000);
        account.setCardType("Mastercard");
        account.setUserId("customer3");

        account.create(account);

        account = new Account();
        account.setAccountId("c3sv");
        account.setAccountType("Saving");
        account.setBalance(1000);
        account.setCardType("Mastercard");
        account.setUserId("customer3");

        account.create(account);
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }


    public static List<Account> getAccountsOfUser(String userId) throws IOException {
        Account account = new Account();
        List<Account> accounts = new ArrayList<>();
        accounts = account.readAll().stream().filter(a -> a.userId.equalsIgnoreCase(userId)).toList();
        return accounts;
    }

    public boolean withdraw(double amount) throws IOException {
        Overdraft overdraft = new Overdraft();
        if(this.balance > -100 && !this.isLocked){
            if(this.balance < 0) {
                overdraft.chargeOverdraft(this.accountId, 35);
            }
                this.balance -= amount;
                Transaction transaction = new Transaction();
                transaction.logWithdraw(this, amount);
                this.update(this);
            }else {
            return false;
        }

        return true;
    }

    public boolean deposit(double amount){
        this.balance += amount;
        Transaction transaction = new Transaction();
        transaction.logDeposit(this, amount);
        this.update(this);
        return true;
    }

    public boolean transfer(double amount, Account toAccount){
        Transaction transaction = new Transaction();
        transaction.logTransfer(this, toAccount, amount);
        return this.transferSend(amount, toAccount) && toAccount.transferReceive(amount, this);
    }

    private boolean transferSend(double amount, Account toAccount){
        this.balance -= amount;
        return update(this);
    }

    private boolean transferReceive(double amount, Account fromAccount){
        this.balance += amount;
        return update(this);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", userId='" + userId + '\'' +
                ", balance=" + balance +
                ", accountType='" + accountType + '\'' +
                ", cardType='" + cardType + '\'' +
                ", isLocked=" + isLocked +
                '}';
    }
}
