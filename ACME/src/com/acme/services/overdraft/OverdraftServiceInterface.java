package com.acme.services.overdraft;

import com.acme.entities.Overdraft;
import com.acme.entities.Overdraft;

import java.io.IOException;
import java.util.ArrayList;

public interface OverdraftServiceInterface {
    public boolean create(Overdraft overdraft) throws IOException;
    public Overdraft read(String overdraftId) throws IOException ;
    public ArrayList<Overdraft> readAll() throws IOException ;
    public boolean delete(Overdraft overdraft) throws IOException ;
    public boolean update(Overdraft overdraft) throws IOException ;
}
