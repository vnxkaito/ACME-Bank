package com.acme.entities;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    Account testAccount;
    @BeforeEach
    public void setUp() throws IOException {
        testAccount = new Account().read("testAccount");
    }

    @Test(expected = RuntimeException.class)
    @DisplayName("Testing creation and reading")
    public final void whenAmountIsWithdrawnItsDeductedFromAccount(){
        double balanceBefore = testAccount.getBalance();
        double withdrawalAmount = 50;

        try{
            testAccount.withdraw(withdrawalAmount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        double balanceAfter = testAccount.getBalance();

        assertEquals(balanceBefore, (balanceAfter + withdrawalAmount));
    }

}