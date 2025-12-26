package com.acme.entities;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    Account testAccount;
    Account testAccount2;
    @BeforeEach
    public void setUp() throws IOException {
        testAccount = new Account().read("testAccount");
        testAccount2 = new Account().read("testAccount2");
        setTestAccountBalance(100);
    }

    @Test(expected = RuntimeException.class)
    @DisplayName("Success withdrawal")
    public final void whenAmountIsWithdrawnItsDeductedFromAccount() throws IOException {
        double balanceBefore = testAccount.getBalance();
        double withdrawalAmount = 50;

        try{
            testAccount.withdraw(withdrawalAmount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        double balanceAfter = testAccount.read(testAccount.getAccountId()).getBalance();

        assertEquals(balanceBefore, (balanceAfter + withdrawalAmount));
    }

    @Test(expected = RuntimeException.class)
    @DisplayName("When withdrawing from account with balance -100 or less reject withdrawal")
    public final void whenBalanceIsLessOrEqualMinusHundredRejectWithDrawal() throws IOException {
        setTestAccountBalance(-100);
        testAccount.withdraw(10);
        testAccount = testAccount.read(testAccount.getAccountId());
        assertNotEquals(-110, testAccount.getBalance(), 0.0);
    }

    @Test(expected = RuntimeException.class)
    @DisplayName("After depositing the balance increased with the amount deposited")
    public final void afterDepositingTheBalanceIncreasedWithTheAmountDeposited() throws IOException {
        setTestAccountBalance(0);
        double depositAmount = 50;
        this.testAccount.deposit(depositAmount);
        testAccount = testAccount.read(testAccount.getAccountId());
        assertEquals(depositAmount, testAccount.getBalance());
    }

    @Test(expected = RuntimeException.class)
    @DisplayName("After transferring, the balance is deducted from one account and added to another")
    public final void transferPositiveTest() throws IOException{
        setTestAccountBalance(100);
        setTestAccount2Balance(30);
        testAccount.transfer(10, testAccount2);
        testAccount2 = testAccount2.read(testAccount2.getAccountId());
        assertTrue(testAccount.getBalance() == 90 && testAccount2.getBalance() == 40);
    }

    private void setTestAccountBalance(double amount){
        this.testAccount.setBalance(amount);
        this.testAccount.update(this.testAccount);
    }

    private void setTestAccount2Balance(double amount){
        this.testAccount2.setBalance(amount);
        this.testAccount2.update(this.testAccount2);
    }

}