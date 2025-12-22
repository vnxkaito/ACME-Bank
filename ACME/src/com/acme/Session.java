package com.acme;

import com.acme.entities.User;
import com.acme.services.account.AccountService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Session {
    User loggedUser;
    int loginAttempts = 0;
    LocalDateTime cooldown;

    public boolean login(String userId, String password) throws IOException {
        if(checkCoolDown()){
            return false;
        }
        User user = new User();
        PasswordEncryption enc = new PasswordEncryption();
        user = user.read(userId);
        if(user.getPassword().equalsIgnoreCase(enc.encryptPassword(password))){
            loggedUser = user;
            return true;
        }else{
            incrementAttempts();
            return false;
        }
    }

    private void incrementAttempts(){
        loginAttempts++;
        if(loginAttempts >= 3){
            applyCooldown();
        }
    }
    private boolean checkCoolDown(){
        if(!(cooldown.isBefore(LocalDateTime.now().minusMinutes(1)))){
            System.out.println("Too many attempts, please wait");
            return true;
        }else{
            return false;
        }
    }
    private void applyCooldown(){
        this.cooldown = LocalDateTime.now();
    }
}
