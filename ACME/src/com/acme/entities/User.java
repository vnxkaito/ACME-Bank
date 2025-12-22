package com.acme.entities;

import com.acme.PasswordEncryption;
import com.acme.services.user.UserService;

import java.io.IOException;

public class User extends UserService {
    private String userId;
    private String password;
    private String role;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public User(String userId, String password, String role, String name){
        PasswordEncryption enc = new PasswordEncryption();
        this.userId = userId;
        this.password = enc.encryptPassword(password);
        this.role = role;
        this.name = name;
    }

    public String getFileName(){
        return role + "-" + name + "-" + userId;
    }

    public static void main(String[] args){
        User user = new User("testId", "testPassword", "testRole", "Ali");
        try {
            user.create(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
