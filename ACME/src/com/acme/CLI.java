package com.acme;

import com.acme.entities.Account;
import com.acme.entities.User;

import java.io.IOException;
import java.util.ArrayList;
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
            case 0:
                System.exit(0);

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
            case 9:
                logout();
            case 0:
                System.exit(0);

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
            case 9:
                logout();
            case 0:
                System.exit(0);

        }
    }
    public void showCustomerMenu() throws IOException {
        pleaseChooseText();
        System.out.println("1) view accounts");
        System.out.println("2) withdraw");
        System.out.println("3) deposit");
        System.out.println("4) transfer");
        System.out.println("5) change password");
        System.out.println("9) logout");
        System.out.println("0) exit");
        int input = scn.nextInt();
        switch(input){
            case 9:
                logout();
            case 0:
                System.exit(0);

        }
    }

    public void logout() throws IOException {
        this.session.logout();
        showMainMenu();
    }

    public void withdraw() throws IOException {
        String accountId = chooseAccount();
        Account account = new Account();
        account = account.read(accountId);
        double amount = 0;
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
        double amount = 0;
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
        double amount = 0;
        if(account.transfer(amount, toAccount)){
            System.out.println("done");
        }else{
            System.out.println("failed");
        }
        showMainMenu();
    }


    public void changePassword() throws IOException {

        showMainMenu();
    }

    public void linkAccountToUser() throws IOException {
        showMainMenu();

    }

    public void displayAccountBalance() throws IOException {
        showMainMenu();

    }

    public void displayAccountStatement() throws IOException {
        showMainMenu();

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

    public String chooseAccount(){
        ArrayList<Account> accounts = new ArrayList<>();
        if(session.loggedUser.getUserId() != null){
            accounts = Account.getAccountsOfUser(session.loggedUser.getUserId());
            if(!accounts.isEmpty()){
                System.out.println("Please choose an account from below by entering its number:");
                for(int i = 1; i < accounts.size(); i++){
                    System.out.println(i + ") " + accounts.get(i).getAccountId());
                }
                int input = scn.nextInt();
                return accounts.get(input).getAccountId();
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


}
