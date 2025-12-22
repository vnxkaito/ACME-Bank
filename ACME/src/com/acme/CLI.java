package com.acme;

import java.io.IOException;
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
    public void showMainMenu(){
        welcomeText();
        if(session.loggedUser.getUserId() == null){
            showNonLoggedInMenu();
        }else{
            showLoggedInMenu();
        }

    }

    public void showNonLoggedInMenu(){
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

    public void showLoggedInMenu(){

        showMainMenu();
    }


    public void showLoginMenu(){
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

    public void showAdminMenu(){
        pleaseChooseText();
        System.out.println("1) create user");
        System.out.println("2) create account");
        System.out.println("3) link account to user");
        System.out.println("0) exit");
        int input = scn.nextInt();
        switch(input){
            case 0:
                System.exit(0);

        }
    }
    public void showBankerMenu(){
        pleaseChooseText();
        System.out.println("1) create account");
        System.out.println("2) link account to user");
        System.out.println("3) display account balance");
        System.out.println("4) display account statement");
        System.out.println("0) exit");
        int input = scn.nextInt();
        switch(input){
            case 0:
                System.exit(0);

        }
    }
    public void showCustomerMenu(){
        pleaseChooseText();
        System.out.println("1) view accounts");
        System.out.println("2) withdraw");
        System.out.println("3) deposit");
        System.out.println("4) transfer");
        System.out.println("0) exit");
        int input = scn.nextInt();
        switch(input){
            case 0:
                System.exit(0);

        }
    }
}
