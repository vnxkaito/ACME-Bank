package com.acme.entities;

import com.acme.PasswordEncryption;
import com.acme.services.user.UserService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.IOException;
import java.util.ArrayList;

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

    public User(){};

    public User(String userId, String password, String role, String name){
        PasswordEncryption enc = new PasswordEncryption();
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    public static boolean doesUserExist(String userId) throws IOException {
        User user = new User();
        ArrayList<User> users = user.readAll();
        return !users.stream().filter(u -> u.getUserId().equalsIgnoreCase(userId)).toList().isEmpty();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @JsonIgnore
    public String getFileName(){
        return role + "-" + name + "-" + userId;
    }

    public static void main(String[] args){
        User user = new User("testId", "testPassword", "Admin", "Ali");
        try {
            user.create(user);
//            User testUser = user.read("testId");
//            ArrayList<User> users = user.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
