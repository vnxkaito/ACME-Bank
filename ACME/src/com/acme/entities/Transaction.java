package com.acme.entities;

import com.acme.services.transaction.TransactionService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Transaction extends TransactionService {
    private String transactionId;
    private Optional<LocalDateTime> timestamp;
    private String fromAccountId = "";
    private String toAccountId = "";
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

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
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

    public static String generateId(){
        Transaction transaction = new Transaction();
        boolean isValid = false;
        int random;
        do{
            random = java.util.concurrent.ThreadLocalRandom.current().nextInt(10_000_000, 100_000_000);
            try{
                transaction = transaction.read(("T"+random));
                if(transaction.getTransactionId() == null){
                    isValid = true;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }while(!isValid);
        return "T"+random;
    }

    public void logWithdraw(Account fromAccount, double amount){
        Transaction transaction = new Transaction();
        transaction.setTimestamp(Optional.of(LocalDateTime.now()));
        transaction.setAmount(amount);
        transaction.setFromAccountId(fromAccount.getAccountId());
        transaction.setToAccountId("");
        transaction.setType("withdraw");

        try {
            transaction.create(transaction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logDeposit(Account toAccount, double amount){
        Transaction transaction = new Transaction();
        transaction.setTimestamp(Optional.of(LocalDateTime.now()));
        transaction.setAmount(amount);
        transaction.setToAccountId(toAccount.getAccountId());
        transaction.setFromAccountId("");
        transaction.setType("deposit");

        try {
            transaction.create(transaction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logTransfer(Account fromAccount, Account toAccount, double amount){
        Transaction transaction = new Transaction();
        transaction.setTimestamp(Optional.of(LocalDateTime.now()));
        transaction.setAmount(amount);
        transaction.setFromAccountId(fromAccount.getAccountId());
        transaction.setToAccountId(toAccount.getAccountId());
        transaction.setType("transfer");

        try {
            transaction.create(transaction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Transaction> getPastDaysTransactions(String accountId, int days) throws IOException {
        Transaction transaction = new Transaction();
        List<Transaction> transactions = new ArrayList<>();
        transactions = transaction.readAll().stream().toList();
        transactions = transactions.stream().filter(t->t.getTimestamp().isPresent())
                .filter(t->t.getTimestamp().get().isAfter(LocalDateTime.now().minusDays(days)))
                .filter(t->(t.getToAccountId().equalsIgnoreCase(accountId))||(t.getFromAccountId().equalsIgnoreCase(accountId)))
                .toList();
        return new ArrayList<>(transactions);
    }

    public ArrayList<Transaction> getCustomPeriodTransactions(String accountId, int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) throws IOException {
        Transaction transaction = new Transaction();
        List<Transaction> transactions = new ArrayList<>();
        LocalDateTime startDate = LocalDateTime.of(startYear, startMonth, startDay, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(endYear, endMonth, endDay, 0, 0, 0);
        transactions = transaction.readAll().stream().toList();
        transactions = transactions.stream().filter(t->t.getTimestamp().isPresent())
                .filter(t->t.getTimestamp().get().isAfter(startDate) || t.getTimestamp().get().isEqual(startDate) )
                .filter(t->t.getTimestamp().get().isBefore(endDate))
                .filter(t->(t.getToAccountId().equalsIgnoreCase(accountId))||(t.getFromAccountId().equalsIgnoreCase(accountId)))
                .toList();
        return new ArrayList<>(transactions);
    }

    public ArrayList<Transaction> getTodayTransactions(String accountId) throws IOException {
        return getCustomPeriodTransactions(
                accountId,
          LocalDateTime.now().getYear(),
          LocalDateTime.now().getMonthValue(),
                LocalDateTime.now().getDayOfMonth(),
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonthValue(),
                LocalDateTime.now().getDayOfMonth()
        );
    }

    public ArrayList<Transaction> getYesterdayTransactions(String accountId) throws IOException {
        return getCustomPeriodTransactions(
                accountId,
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonthValue(),
                LocalDateTime.now().getDayOfMonth() - 1,
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonthValue(),
                LocalDateTime.now().getDayOfMonth() - 1
        );
    }

    public void printPastDaysStatement(String accountId, int days) throws IOException {
        Transaction transaction = new Transaction();
        transaction.getPastDaysTransactions(accountId, days).forEach(System.out::println);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", timestamp=" + timestamp +
                ", fromAccountId='" + fromAccountId + '\'' +
                ", toAccountId='" + toAccountId + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                '}';
    }


    public void printTodayTransactions(String accountId) throws IOException {
        getTodayTransactions(accountId).forEach(System.out::println);
    }

    public void printYesterdayTransactions(String accountId) throws IOException {
        getYesterdayTransactions(accountId).forEach(System.out::println);
    }

    public void printCustomPeriodStatement(String accountId, int[] period) throws IOException {
        getCustomPeriodTransactions(accountId, period[0], period[1], period[2], period[3], period[4], period[5])
                .forEach(System.out::println);
    }
}

