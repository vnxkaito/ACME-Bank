package com.acme.services.overdraft;

import com.acme.entities.Overdraft;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OverdraftService implements OverdraftServiceInterface {
    ObjectMapper mapper = new ObjectMapper();
    @Override
    public boolean create(Overdraft overdraft) throws IOException {
        String json = mapper.writeValueAsString(overdraft);
        mapper.writeValue(new File("data/overdrafts/"+overdraft.getOverdraftId()+".json"), overdraft);
        return true;
    }

    @Override
    public Overdraft read(String overdraftId) throws IOException {
        List<Overdraft> overdraftsList = new ArrayList<>();
        Overdraft overdraft = new Overdraft();
        try{
            overdraftsList = readAll().stream().filter(u -> u.getOverdraftId().equalsIgnoreCase(overdraftId)).toList();
            if(!overdraftsList.isEmpty()){
                overdraft = overdraftsList.get(0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return overdraft;
    }

    @Override
    public ArrayList<Overdraft> readAll() throws IOException {
        ArrayList<Overdraft> overdrafts = new ArrayList<>();

        File dir = new File("data/overdrafts");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if(files != null){
            for(File file: files){
                String url = "data/overdrafts/"+file.getName();
                Overdraft overdraft = mapper.readValue(new File(url), Overdraft.class);
                overdrafts.add(overdraft);
            }
        }
        return overdrafts;
    }

    @Override
    public boolean delete(Overdraft overdraft) {
        return false;
    }

    @Override
    public boolean update(Overdraft overdraft) {
        File file = new File("data/overdrafts/"+overdraft.getOverdraftId()+".json");
        if(!file.exists()){
            return false;
        }
        try{
            mapper.writeValue(file, overdraft);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
