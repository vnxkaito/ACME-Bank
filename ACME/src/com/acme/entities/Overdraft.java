package com.acme.entities;
import com.acme.services.overdraft.OverdraftService;

import java.io.IOException;

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

    public Overdraft(){

    }

    public String genOverdraftId(){
        return "ov" + System.currentTimeMillis();
    }

    public static int getOverdraftCountForAccount(String accountId) throws IOException {
        Overdraft overdraft = new Overdraft();
        return overdraft.readAll().stream().filter(ov -> ov.getAccountId().equalsIgnoreCase(accountId)).toList().size();
    }

    public void chargeOverdraft(String accountId, double amount) throws IOException {
        Account account = new Account();
        Overdraft overdraft = new Overdraft();
        account = account.read(accountId);
        overdraft.setAccountId(accountId);
        overdraft.setAmount(amount);
        overdraft.setPaid(false);
        overdraft.create(overdraft);
        System.out.println("Overdraft is charged");
        if(getOverdraftCountForAccount(accountId) >= 2){
            System.out.println("2 overdrafts, account is getting locked/deactivated");
            lockAccount(accountId);
        }
    }

    public void lockAccount(String accountId) throws IOException {
        Account account = new Account();
        account = account.read(accountId);
        account.setLocked(true);
    }

}