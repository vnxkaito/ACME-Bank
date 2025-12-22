package com.acme.services.user;

import com.acme.entities.User;

import java.util.ArrayList;


import com.acme.services.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.IOException;

public class UserService implements UserServiceInterface {
    ObjectMapper mapper = new ObjectMapper();
    @Override
    public boolean create(User user) throws IOException {
        String json = mapper.writeValueAsString(user);
        mapper.writeValue(new File("data/users/"+user.getFileName()+".json"), user);
        return true;
    }

    @Override
    public User read(String userId) throws IOException {
        User user;
        try{
            user = readAll().stream().filter(u -> u.getUserId().equalsIgnoreCase(userId)).toList().get(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public ArrayList<User> readAll() throws IOException {
        ArrayList<User> users = new ArrayList<>();

        File dir = new File("data/users");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if(files != null){
            for(File file: files){
                User user = mapper.readValue(file, User.class);
                users.add(user);
            }
        }
        return users;
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
