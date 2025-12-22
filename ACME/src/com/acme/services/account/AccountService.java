package com.acme.services.account;

import com.acme.PasswordEncryption;
import com.acme.entities.Account;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountService implements AccountServiceInterface{
    ObjectMapper mapper = new ObjectMapper();
    @Override
    public boolean create(Account account) throws IOException {
        String json = mapper.writeValueAsString(account);
        mapper.writeValue(new File("data/accounts/"+account.getAccountId()+".json"), account);
        return true;
    }

    @Override
    public Account read(String accountId) throws IOException {
        List<Account> accountsList = new ArrayList<>();
        Account account = new Account();
        try{
            accountsList = readAll().stream().filter(u -> u.getAccountId().equalsIgnoreCase(accountId)).toList();
            if(!accountsList.isEmpty()){
                account = accountsList.get(0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return account;
    }

    @Override
    public ArrayList<Account> readAll() throws IOException {
        ArrayList<Account> accounts = new ArrayList<>();

        File dir = new File("data/accounts");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if(files != null){
            for(File file: files){
                String url = "data/accounts/"+file.getName();
                Account account = mapper.readValue(new File(url), Account.class);
                accounts.add(account);
            }
        }
        return accounts;
    }

    @Override
    public boolean delete(Account account) {
        return false;
    }

    @Override
    public boolean update(Account account) {
        File file = new File("data/accounts/"+account.getAccountId()+".json");
        if(!file.exists()){
            return false;
        }
        try{
            mapper.writeValue(file, account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
