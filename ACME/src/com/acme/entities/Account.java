package com.acme.entities;

import com.acme.services.account.AccountService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Account extends AccountService {
    private String accountId;
    private String userId;
    private double balance;
    private String accountType;
    private String cardType;
    private boolean isLocked;

    public Account() {

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
        if (!checkWithdrawLimit(this.accountId, amount)) {
            System.out.println("Limit reached");
            return false;
        }
        Overdraft overdraft = new Overdraft();
        if ( (this.balance - amount) > -100 && !this.isLocked) {
            this.balance -= amount;
            Transaction transaction = new Transaction();
            transaction.logWithdraw(this, amount);
            this.update(this);
            if (this.balance < 0) {
                overdraft.chargeOverdraft(this.accountId, 35);
            }
        } else if(this.isLocked) {
            System.out.println("Account is locked");
            return false;
        } else if(this.balance <= -100) {
            System.out.println("Insufficient balance");
            return false;
        }else{
                return false;
            }


        return true;
    }

    public boolean deposit(double amount) throws IOException {
        if (!checkDepositLimit(this.accountId, amount)) {
            System.out.println("Limit reached");
            return false;
        }
        this.balance += amount;
        Transaction transaction = new Transaction();
        transaction.logDeposit(this, amount);
        if((Overdraft.getUnpaidOverdraftCount(this.accountId) == 0) && (this.balance >= 0)){
            this.isLocked = false;
        }
        this.update(this);
        return true;
    }

    public boolean transfer(double amount, Account toAccount) throws IOException {
        if (!checkTransferLimit(this.accountId, amount)) {
            System.out.println("Limit reached");
            return false;
        }
        Transaction transaction = new Transaction();
        transaction.logTransfer(this, toAccount, amount);
        return this.transferSend(amount, toAccount) && toAccount.transferReceive(amount, this);
    }

    private boolean transferSend(double amount, Account toAccount) {
        this.balance -= amount;
        return update(this);
    }

    private boolean transferReceive(double amount, Account fromAccount) {
        this.balance += amount;
        return update(this);
    }

    private boolean checkWithdrawLimit(String fromAccountId, double amount) throws IOException {
        double limit = getCardTypeLimit("withdraw", false);
        double usage = checkTodayWithdrawUsage(fromAccountId);
        return (usage + amount) < limit;
    }

    private boolean checkDepositLimit(String toAccountId, double amount) throws IOException {
        boolean toOwn = checkSameOwner(this.accountId, toAccountId);
        double limit = getCardTypeLimit("deposit", toOwn);
        double usage = checkTodayDepositUsage(accountId, toOwn);
        return (usage + amount) < limit;
    }

    private boolean checkTransferLimit(String toAccountId, double amount) throws IOException {
        boolean toOwn = checkSameOwner(this.accountId, toAccountId);
        double limit = getCardTypeLimit("transfer", toOwn);
        double usage = checkTodayTransferUsage(accountId, toOwn);
        return (usage + amount) < limit;
    }

    private double checkTodayWithdrawUsage(String accountId) throws IOException {
        Transaction transaction = new Transaction();
        double amount = 0;
        String type = "withdraw";
        List<Transaction> transactions =
                transaction.getCustomPeriodTransactionsForType(accountId, transaction.getTodayStartTimeStamp(), LocalDateTime.now(), type);

        amount = transactions.stream().mapToDouble(Transaction::getAmount).sum();

        return amount;

    }

    private double checkTodayDepositUsage(String accountId, boolean toOwn) throws IOException {
        Transaction transaction = new Transaction();
        double amount = 0;
        String type = "deposit";
        List<Transaction> transactions =
                transaction.getCustomPeriodTransactionsForType(accountId, transaction.getTodayStartTimeStamp(), LocalDateTime.now(), type);
        if (toOwn) {
            transactions = transactions.stream().filter(t -> {
                try {
                    return checkSameOwner(accountId, t.getToAccountId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).toList();
        }
        amount = transactions.stream().mapToDouble(Transaction::getAmount).sum();

        return amount;
    }

    private double checkTodayTransferUsage(String accountId, boolean toOwn) throws IOException {
        Transaction transaction = new Transaction();
        double amount = 0;
        String type = "transfer";
        List<Transaction> transactions =
                transaction.getCustomPeriodTransactionsForType(accountId, transaction.getTodayStartTimeStamp(), LocalDateTime.now(), type);

        if (toOwn) {
            transactions = transactions.stream().filter(t -> {
                try {
                    return (checkSameOwner(t.getFromAccountId(), t.getToAccountId())
                            && (this.accountId.equalsIgnoreCase(t.getFromAccountId())
                    ));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).toList();
        }

        amount = transactions.stream().mapToDouble(Transaction::getAmount).sum();

        return amount;
    }

    public double getCardTypeLimit(String type, boolean isOwn) {
        if (type.equalsIgnoreCase("withdraw")) {
            if (this.cardType.equalsIgnoreCase("mastercard")) {
                return 5000;
            } else if (this.cardType.equalsIgnoreCase("mastercard titanium")) {
                return 10000;
            } else if (this.cardType.equalsIgnoreCase("mastercard platinum")) {
                return 20000;
            }
        } else if (type.equalsIgnoreCase("deposit")) {
            if (isOwn) {
                // Deposit to own
                if (this.cardType.equalsIgnoreCase("mastercard")) {
                    return 200000;
                } else if (this.cardType.equalsIgnoreCase("mastercard titanium")) {
                    return 200000;
                } else if (this.cardType.equalsIgnoreCase("mastercard platinum")) {
                    return 200000;
                }
            } else {
                // Deposit
                if (this.cardType.equalsIgnoreCase("mastercard")) {
                    return 100000;
                } else if (this.cardType.equalsIgnoreCase("mastercard titanium")) {
                    return 100000;
                } else if (this.cardType.equalsIgnoreCase("mastercard platinum")) {
                    return 100000;
                }
            }

        } else if (type.equalsIgnoreCase("transfer")) {
            if (isOwn) {
                // Transfer to own
                if (this.cardType.equalsIgnoreCase("mastercard")) {
                    return 20000;
                } else if (this.cardType.equalsIgnoreCase("mastercard titanium")) {
                    return 40000;
                } else if (this.cardType.equalsIgnoreCase("mastercard platinum")) {
                    return 80000;
                }
            } else {
                // Deposit
                if (this.cardType.equalsIgnoreCase("mastercard")) {
                    return 10000;
                } else if (this.cardType.equalsIgnoreCase("mastercard titanium")) {
                    return 20000;
                } else if (this.cardType.equalsIgnoreCase("mastercard platinum")) {
                    return 40000;
                }
            }
        }
        return 0;

    }

    public boolean doesAccountExist(String accountId) throws IOException {
        return new Account().read(accountId).getAccountId() != null;
    }

    private static boolean checkSameOwner(String accountIdOne, String accountIdTwo) throws IOException {
        Account accountOne = new Account().read(accountIdOne);
        Account accountTwo = new Account().read(accountIdTwo);
        return accountOne.getUserId().equalsIgnoreCase(accountTwo.getUserId());
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
