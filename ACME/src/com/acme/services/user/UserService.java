package com.acme.services.user;

import com.acme.entities.User;

import java.util.ArrayList;


import com.acme.services.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class UserService implements UserServiceInterface {
    ObjectMapper mapper = new ObjectMapper();
    @Override
    public boolean create(User user) throws IOException {
        String json = mapper.writeValueAsString(user);
        mapper.writeValue(new File("data/users/"+user.getUserId()+".json"), user);
        return true;
    }

    @Override
    public User read(String userId) throws IOException {
        return mapper.readValue(new File("data/users/"+userId+".json"), User.class);
    }

    @Override
    public ArrayList<User> readAll() {
        return null;
    }

    @Override
    public boolean delete(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }
}
