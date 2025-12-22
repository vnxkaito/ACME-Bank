package com.acme.services.transaction;

import com.acme.entities.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionService implements TransactionServiceInterface{
    ObjectMapper mapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jdk8.Jdk8Module())
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    @Override
    public boolean create(Transaction transaction) throws IOException {
        transaction.setTransactionId(Transaction.generateId());
        mapper.writeValue(new File("data/transactions/"+transaction.getTransactionId()+".json"), transaction);
        return true;
    }

    @Override
    public Transaction read(String transactionId) throws IOException {
        List<Transaction> transactionsList = new ArrayList<>();
        Transaction transaction = new Transaction();
        try{
            transactionsList = readAll().stream().filter(u -> u.getTransactionId().equalsIgnoreCase(transactionId)).toList();
            if(!transactionsList.isEmpty()){
                transaction = transactionsList.get(0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return transaction;
    }

    @Override
    public ArrayList<Transaction> readAll() throws IOException {
        ArrayList<Transaction> transactions = new ArrayList<>();

        File dir = new File("data/transactions");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if(files != null){
            for(File file: files){
                String url = "data/transactions/"+file.getName();
                Transaction transaction = mapper.readValue(new File(url), Transaction.class);
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    @Override
    public boolean delete(Transaction transaction) {
        return false;
    }

    @Override
    public boolean update(Transaction transaction) {
        File file = new File("data/transactions/"+transaction.getTransactionId()+".json");
        if(!file.exists()){
            return false;
        }
        try{
            mapper.writeValue(file, transaction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
