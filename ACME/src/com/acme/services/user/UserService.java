package com.acme.services.user;

import com.acme.PasswordEncryption;
import com.acme.entities.User;

import java.util.ArrayList;


import com.acme.services.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserService implements UserServiceInterface {
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean create(User user) throws IOException {
        String json = mapper.writeValueAsString(user);
        PasswordEncryption enc = new PasswordEncryption();
        user.setPassword(enc.encryptPassword(user.getPassword()));
        mapper.writeValue(new File("data/users/" + user.getFileName() + ".json"), user);
        return true;
    }

    @Override
    public User read(String userId) throws IOException {
        List<User> userList = new ArrayList<>();
        User user = new User();
        try {
            userList = readAll().stream().filter(u -> u.getUserId().equalsIgnoreCase(userId)).toList();
            if (!userList.isEmpty()) {
                user = userList.get(0);
            }
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
        if (files != null) {
            for (File file : files) {
                String url = "data/users/" + file.getName();
                User user = mapper.readValue(new File(url), User.class);
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public boolean delete(User user) {
         return new File("data/users/"+user.getFileName()+".json").delete();
    }

    @Override
    public boolean update(User user) throws IOException {
        File file = new File("data/users/" + user.getFileName() + ".json");
        if (!file.exists()) {
            return false;
        }
        PasswordEncryption enc = new PasswordEncryption();
        user.setPassword(enc.encryptPassword(user.getPassword()));
        mapper.writeValue(new File("data/users/" + user.getFileName() + ".json"), user);
        return true;

    }
}
