package com.acme;

import com.acme.entities.Account;
import com.acme.entities.Overdraft;
import com.acme.entities.Transaction;
import com.acme.entities.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLI {
    Session session = new Session();
    Scanner scn = new Scanner(System.in);

    public void welcomeText(){
        System.out.println("\n---------------------------------------");
        System.out.println("Welcome to ACME bank");
    }
    public void pleaseChooseText(){
        System.out.println("\nPlease choose a number from the below menu");
    }
    public void showMainMenu() throws IOException {
        welcomeText();
        if(session.loggedUser.getUserId() == null){
            showNonLoggedInMenu();
        }else{
            showLoggedInMenu();
        }

    }

    public void showNonLoggedInMenu() throws IOException {
        pleaseChooseText();
        System.out.println("1) login");
        System.out.println("0) exit");
        int input = scn.nextInt();
        switch(input){
            case 1:
                showLoginMenu();
                break;
            case 0:
                System.exit(0);
                break;

        }

    }

    public void showLoggedInMenu() throws IOException {
        String role = session.loggedUser.getRole();
        if(role.equalsIgnoreCase("Admin")) {
            showAdminMenu();
        }
        else if(role.equalsIgnoreCase("Banker")) {
            showBankerMenu();
        }
        else if(role.equalsIgnoreCase("Customer")) {
            showCustomerMenu();
        }else{
            System.out.println("No actions implemented for your role");
           session.logout();
        }

        showMainMenu();
    }


    public void showLoginMenu() throws IOException {
        System.out.println("Please enter your userId");
        String userId = scn.next();
        System.out.println("Please enter your password");
        String password = scn.next();
        try {
            if(session.login(userId, password)){
                System.out.println("Welcome " + session.loggedUser.getName());
            }else{
                System.out.println("Login failed");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        showMainMenu();
    }

    public void showAdminMenu() throws IOException {
        pleaseChooseText();
        System.out.println("1) create user");
        System.out.println("2) create account");
        System.out.println("3) link account to user");
        System.out.println("9) logout");
        System.out.println("0) exit");
        int input = scn.nextInt();
        switch(input){
            case 2:
                createAccount();
                break;
            case 9:
                logout();
                break;
            case 0:
                System.exit(0);
                break;

        }
    }
    public void showBankerMenu() throws IOException {
        pleaseChooseText();
        System.out.println("1) create account");
        System.out.println("2) link account to user");
        System.out.println("3) display account balance");
        System.out.println("4) display account statement");
        System.out.println("9) logout");
        System.out.println("0) exit");
        int input = scn.nextInt();
        switch(input){
            case 1:
                createAccount();
                break;
            case 9:
                logout();
                break;
            case 0:
                System.exit(0);
                break;

        }
    }
    public void showCustomerMenu() throws IOException {
        pleaseChooseText();
        System.out.println("1) view accounts");
        System.out.println("2) withdraw");
        System.out.println("3) deposit");
        System.out.println("4) transfer");
        System.out.println("5) change password");
        System.out.println("6) display account statement");
        System.out.println("7) view overdrafts");
        System.out.println("8) pay overdrafts");
        System.out.println("9) logout");
        System.out.println("0) exit");
        int input = scn.nextInt();
        switch(input){
            case 1:
                viewOwnAccounts();
                break;
            case 2:
                withdraw();
                break;
            case 3:
                deposit();
                break;
            case 4:
                transfer();
                break;
            case 5:
                changePassword();
                break;
            case 6:
                displayAccountStatement(chooseAccount());
                break;
            case 7:
                viewUnpaidOverdrafts(chooseAccount());
            case 8:
                payOverdraftDialog(chooseAccount());
            case 9:
                logout();
                break;
            case 0:
                System.exit(0);
                break;

        }
    }

    public void logout() throws IOException {
        this.session.logout();
        showMainMenu();
    }

    public void viewOwnAccounts() throws IOException {
        Account.getAccountsOfUser(session.loggedUser.getUserId()).forEach(System.out::println);
        showMainMenu();
    }

    public void withdraw() throws IOException {
        String accountId = chooseAccount();
        Account account = new Account();
        account = account.read(accountId);
        System.out.println("Enter the amount:");
        double amount = scn.nextDouble();
        if(account.withdraw(amount)){
            System.out.println("done");
        }else{
            System.out.println("failed");
        }
        showMainMenu();
    }

    public void deposit() throws IOException {
        String accountId = chooseAccount();
        Account account = new Account();
        account = account.read(accountId);
        System.out.println("Enter the amount:");
        double amount = scn.nextDouble();
        if(account.deposit(amount)){
            System.out.println("done");
        }else{
            System.out.println("failed");
        }

        showMainMenu();
    }

    public void transfer() throws IOException {
        String accountId = chooseAccount();
        Account account = new Account();
        account = account.read(accountId);
        Account toAccount = new Account();
        System.out.println("Enter the amount:");
        double amount = scn.nextDouble();
        if(account.transfer(amount, toAccount)){
            System.out.println("done");
        }else{
            System.out.println("failed");
        }
        showMainMenu();
    }


    public void changePassword() throws IOException {
        System.out.println("Enter your new password");
        String newPassword = scn.next();
        User user = new User();
        user = user.read(session.loggedUser.getUserId());
        user.setPassword(newPassword);
        user.update(user);
        showMainMenu();
    }

    public void linkAccountToUser() throws IOException {
        showMainMenu();

    }

    public void displayAccountBalance() throws IOException {
        showMainMenu();

    }

    public void displayAccountStatement(String accountId) throws IOException {
        Transaction transaction = new Transaction();

        System.out.println("Would you like to view the statement for:");
        System.out.println("1) Today");
        System.out.println("2) Yesterday");
        System.out.println("3) Last week");
        System.out.println("4) Last 7 days");
        System.out.println("5) Last Month");
        System.out.println("6) Last 30 days");
        System.out.println("7) custom period");
        int input = scn.nextInt();

        switch (input){
            case 1:
                transaction.getTodayTransactions(accountId).forEach(System.out::println);
                break;
            case 2:
                transaction.getYesterdayTransactions(accountId).forEach(System.out::println);
                break;
            case 3:
                transaction.getLastWeekTransactions(accountId).forEach(System.out::println);
                break;
            case 4:
                transaction.getLast7DaysTransactions(accountId).forEach(System.out::println);
                break;
            case 5:
                transaction.getLastMonthTransactions(accountId).forEach(System.out::println);
                break;
            case 6:
                transaction.getLast30DaysTransactions(accountId).forEach(System.out::println);
            case 7:
                int[] period = getCustomPeriod();
                LocalDateTime startDate = LocalDateTime.of(period[0], period[1], period[2], 0, 0, 0);
                LocalDateTime endDate = LocalDateTime.of(period[3], period[4], period[5], 0, 0, 0);
                transaction.getCustomPeriodTransactions(accountId, startDate, endDate).forEach(System.out::println);
                break;
        }
        showMainMenu();
    }

    public int[] getCustomPeriod(){
        System.out.println("Enter starting year");
        int startingYear = scn.nextInt();
        System.out.println("Enter starting month");
        int startingMonth = scn.nextInt();
        System.out.println("Enter starting day");
        int startingDay = scn.nextInt();
        System.out.println("Enter ending year");
        int endingYear = scn.nextInt();
        System.out.println("Enter ending month");
        int endingMonth = scn.nextInt();
        System.out.println("Enter ending day");
        int endingDay = scn.nextInt();

        int[] output = new int[6];
        output[0] = startingYear;
        output[1] = startingMonth;
        output[2] = startingDay;
        output[3] = endingYear;
        output[4] = endingMonth;
        output[5] = endingDay;

        return output;
    }

    public void createAccount() throws IOException {
        Account account = new Account();

        account.setAccountId(getAccountIdDialog());
        account.setUserId(getUserIdDialog());
        account.setCardType(getCardType());

        System.out.println("Please choose account type:");
        System.out.println("1) Checking");
        System.out.println("2) Saving");
        int typeInput = scn.nextInt();
        if(typeInput == 1){
            account.setAccountType("Checking");
        } else{
            account.setAccountType("Saving");
        }
        System.out.println("Please enter the initial balance: ");
        double initBalance = scn.nextDouble();
        account.setBalance(initBalance);
        account.create(account);
        showMainMenu();
    }

    public String chooseAccount() throws IOException {
        List<Account> accounts = new ArrayList<>();
        if(session.loggedUser.getUserId() != null){
            accounts = Account.getAccountsOfUser(session.loggedUser.getUserId());
            if(!accounts.isEmpty()){
                System.out.println("Please choose an account from below by entering its number:");
                for(int i = 1; i <= accounts.size(); i++){
                    System.out.println(i + ") " + accounts.get(i-1).getAccountId());
                }
                int input = scn.nextInt();
                return accounts.get(input-1).getAccountId();
            }else{
                System.out.println("This user has no accounts");
                return null;
            }
        }else{
            return null;
        }
    }

    public String getCardType(){
        System.out.println("Please choose a credit card type:");
        System.out.println("1) Mastercard");
        System.out.println("2) Mastercard Titanium");
        System.out.println("3) Mastercard Platinum");
        int input = scn.nextInt();
        switch (input){
            case 1:
                return "Mastercard";
            case 2:
                return "Mastercard Titanium";
            case 3:
                return "Mastercard Platinum";
        }
        return "Mastercard";
    }

    public String getAccountIdDialog() throws IOException {
        boolean accountExists = false;
        Account account = new Account();
        System.out.println("Please enter an accountId");
        String accountId = scn.next();
        do{
            accountExists = account.read(accountId).getAccountId() != null;
            if(accountExists){
                System.out.println("This account ID already exist");
            }
        }while(accountExists);
        return accountId;
    }

    public String getUserIdDialog() throws IOException {
        Account account = new Account();
        String userId = "";
        boolean userExists = false;
        do{
            System.out.println("Enter an existing userId");
            userId = scn.next();
            userExists = User.doesUserExist(userId);
            if(userExists){
                return userId;
            }else{
                System.out.println("This userId doesn't exist");
            }
        }while(!userExists);
        return userId;
    }

    public void viewUnpaidOverdrafts(String accountId) throws IOException {
        List<Overdraft> overdrafts = Overdraft.getUnpaidOverdraftsOfAccount(accountId);
        if(overdrafts.isEmpty()){
            System.out.println("This account doesn't any unpaid overdrafts");
        }else{
            for(int i = 0; i < overdrafts.size(); i++){
                System.out.println((i+1)+") " + overdrafts.get(i));
            }

        }
    }

    public void payOverdraftDialog(String accountId) throws IOException {
        List<Overdraft> overdrafts = Overdraft.getUnpaidOverdraftsOfAccount(accountId);
        if(overdrafts.isEmpty()){
            System.out.println("This account doesn't any unpaid overdrafts");
        }else{
            for(int i = 0; i < overdrafts.size(); i++){
                System.out.println((i+1)+") " + overdrafts.get(i));
            }
            int input = scn.nextInt();
            overdrafts.get(input-1).payOverdraft();
        }

    }


}
