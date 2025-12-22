package com.acme;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class PasswordEncryption {
    private String key = "kjh32423%#@$dafjk;dl;";
    StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();;
    public PasswordEncryption(){
        this.enc.setPassword(key);
        this.enc.setAlgorithm("PBEWithMD5AndDES");
    }
    public String encryptPassword(String plainPassword){
        return enc.encrypt(plainPassword);
    }
    public String decryptPassword(String encryptedPassword){
        return enc.decrypt(encryptedPassword);
    }
}
