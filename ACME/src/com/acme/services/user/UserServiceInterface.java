package com.acme.services.user;

import com.acme.entities.User;

import java.io.IOException;
import java.util.ArrayList;

public interface UserServiceInterface {
    public boolean create(User user) throws IOException;
    public User read(String userId) throws IOException ;
    public ArrayList<User> readAll() throws IOException ;
    public boolean delete(User user) throws IOException ;
    public boolean update(User user) throws IOException ;
}
