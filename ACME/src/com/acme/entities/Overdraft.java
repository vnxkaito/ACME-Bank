package com.acme.entities;
import com.acme.services.overdraftIddraft.OverdraftService;

public class Overdraft extends OverdraftService{
    private String overdraftId;
    private String accountId;
    private double amount;
    private boolean isPaid;

    public String getOverdraftId() {
        return overdraftId;
    }

    public void setOverdraftId(String overdraftId) {
        this.overdraftId = overdraftId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public void setAsPaid(){
        this.isPaid = true;
    }




}