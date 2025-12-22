package com.acme.entities;

import java.time.LocalDateTime;
import java.util.Optional;

public class Transaction {
    private String transactionId;
    private Optional<LocalDateTime> timestamp;
    private Account fromAccount = new Account();
    private Account toAccount = new Account();
    private double amount;
    private String type;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Optional<LocalDateTime> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Optional<LocalDateTime> timestamp) {
        this.timestamp = timestamp;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Transaction(){

    }
}
