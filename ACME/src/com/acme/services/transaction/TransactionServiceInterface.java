package com.acme.services.transaction;

import com.acme.entities.Transaction;

import java.io.IOException;
import java.util.ArrayList;

public interface TransactionServiceInterface {
    public boolean create(Transaction overdraft) throws IOException;
    public Transaction read(String transactionId) throws IOException ;
    public ArrayList<Transaction> readAll() throws IOException ;
    public boolean delete(Transaction overdraft) throws IOException ;
    public boolean update(Transaction overdraft) throws IOException ;
}
