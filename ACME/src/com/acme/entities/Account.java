package com.acme.entities;

import com.acme.services.account.AccountService;

import java.io.IOException;
import java.util.ArrayList;

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
        account.setAccountId("testAccount");
        account.setAccountType("CHECKING");
        account.setBalance(1000);
        account.setCardType("MASTERCARD");
        account.setUserId("testId");

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


    public static ArrayList<Account> getAccountsOfUser(String userId){
        ArrayList<Account> accounts = new ArrayList<>();

        return accounts;
    }
}
