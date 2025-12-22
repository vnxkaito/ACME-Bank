package com.acme;

import com.acme.entities.User;
import com.acme.services.account.AccountService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class Session {
    User loggedUser = new User();
    int loginAttempts = 0;
    Optional<LocalDateTime> cooldown = Optional.empty();

    public static void main(String[] args) throws IOException {
        Session session = new Session();
        session.login("testI", "testPassword");
        System.out.println(session.loggedUser.getUserId());
    }

    public boolean login(String userId, String password) throws IOException {
        if(checkCoolDown()){
            return false;
        }
        User user = new User();
        PasswordEncryption enc = new PasswordEncryption();
        user = user.read(userId);
        if(user.getUserId() == null){
            return false;
        }
        if(enc.decryptPassword(user.getPassword()).equalsIgnoreCase(password)){
            loggedUser = user;
            return true;
        }else{
            incrementAttempts();
            return false;
        }
    }

    public void logout(){
        loggedUser = new User();
    }

    private void incrementAttempts(){
        loginAttempts++;
        if(loginAttempts >= 3){
            applyCooldown();
        }
    }
    private boolean checkCoolDown(){
        LocalDateTime cooldownTime;
        if(cooldown.isEmpty()){
            return false;
        }
        if(!(cooldown.get().isBefore(LocalDateTime.now().minusMinutes(1)))){
            System.out.println("Too many attempts, please wait");
            return true;
        }else{
            return false;
        }
    }
    private void applyCooldown(){
        LocalDateTime cooldownTime = LocalDateTime.now();
        this.cooldown = Optional.of(cooldownTime);
    }
}
