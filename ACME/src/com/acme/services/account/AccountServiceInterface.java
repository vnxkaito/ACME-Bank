package com.acme.services.account;

import com.acme.entities.Account;

import java.io.IOException;
import java.util.ArrayList;

public interface AccountServiceInterface {
    public boolean create(Account account) throws IOException;
    public Account read(String userId) throws IOException ;
    public ArrayList<Account> readAll() throws IOException ;
    public boolean delete(Account account) throws IOException ;
    public boolean update(Account account) throws IOException ;
}
