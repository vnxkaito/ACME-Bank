package com.acme.entities;

import com.acme.PasswordEncryption;
import com.acme.services.user.UserService;

import java.io.IOException;

public class User extends UserService {
    private String userId;
    private String password;
    private String role;

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


    public User(String userId, String password, String role){
        PasswordEncryption enc = new PasswordEncryption();
        this.userId = userId;
        this.password = enc.encryptPassword(password);
        this.role = role;
    }

    public static void main(String[] args){
        User user = new User("test", "test", "test");
        try {
            User.create(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
