package com.acme.entities;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    Account testAccount;
    Account testAccount2;

    Account testAccount1_1;
    Account testAccount1_2;
    Account testAccount1_3;
    Account testAccount2_1;
    Account testAccount2_2;
    Account testAccount2_3;
    @BeforeEach
    public void setUp() throws IOException {
        testAccount = new Account().read("testAccount");
        testAccount2 = new Account().read("testAccount2");

        testAccount1_1 = new Account().read("testAccount1_1"); // MASTERCARD
        testAccount1_1 = new Account().read("testAccount1_2"); // MASTERCARD TITANIUM
        testAccount1_1 = new Account().read("testAccount1_3"); // MASTERCARD PLATINUM

        testAccount1_1 = new Account().read("testAccount2_1"); // MASTERCARD
        testAccount1_1 = new Account().read("testAccount2_2"); // MASTERCARD TITANIUM
        testAccount1_1 = new Account().read("testAccount2_3"); // MASTERCARD PLATINUM
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

    // ----- TESTING CARDS LIMITS ------ //

    // ----- withdraw ------ //
    @Test(expected = RuntimeException.class)
    @DisplayName("withdrawal limit mastercard")
    public final void WLM() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard", false);
        boolean failWhenMoreThanLimit = !testAccount1_1.withdraw(limit + 1);
        boolean successWhenLessThanLimit = testAccount1_1.withdraw(limit - 1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);
    }

    @Test(expected = RuntimeException.class)
    @DisplayName("withdrawal limit mastercard titanium")
    public final void WLMT() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard titanium", false);
        boolean failWhenMoreThanLimit = !testAccount1_2.withdraw(limit + 1);
        boolean successWhenLessThanLimit = testAccount1_2.withdraw(limit - 1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);
    }

    @Test(expected = RuntimeException.class)
    @DisplayName("withdrawal limit mastercard platinum")
    public final void WLMP() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard platinum", false);
        boolean failWhenMoreThanLimit = !testAccount1_3.withdraw(limit + 1);
        boolean successWhenLessThanLimit = testAccount1_3.withdraw(limit - 1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);
    }

    // ----- deposit ------ //
    @Test(expected = RuntimeException.class)
    @DisplayName("deposit limit mastercard")
    public final void DLM() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard", false);
        boolean failWhenMoreThanLimit = !testAccount2_1.deposit(limit + 1);
        boolean successWhenLessThanLimit = testAccount2_1.deposit(limit - 1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);
    }

    @Test(expected = RuntimeException.class)
    @DisplayName("deposit limit mastercard titanium")
    public final void DLMT() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard titanium", false);
        boolean failWhenMoreThanLimit = !testAccount2_2.deposit(limit + 1);
        boolean successWhenLessThanLimit = testAccount2_2.deposit(limit - 1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);

    }

    @Test(expected = RuntimeException.class)
    @DisplayName("deposit limit mastercard platinum")
    public final void DLMP() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard platinum", false);
        boolean failWhenMoreThanLimit = !testAccount2_3.deposit(limit + 1);
        boolean successWhenLessThanLimit = testAccount2_3.deposit(limit - 1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);
    }

    // ----- deposit to own ------ //
    @Test(expected = RuntimeException.class)
    @DisplayName("deposit to own limit mastercard")
    public final void DLMO() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard", true);
        boolean failWhenMoreThanLimit = !testAccount2_1.deposit(limit + 1);
        boolean successWhenLessThanLimit = testAccount2_1.deposit(limit - 1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);
    }

    @Test(expected = RuntimeException.class)
    @DisplayName("deposit to own limit mastercard titanium")
    public final void DLMTO() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard titanium", true);
        boolean failWhenMoreThanLimit = !testAccount2_2.deposit(limit + 1);
        boolean successWhenLessThanLimit = testAccount2_2.deposit(limit - 1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);

    }

    @Test(expected = RuntimeException.class)
    @DisplayName("deposit to own limit mastercard platinum")
    public final void DLMPO() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard platinum", true);
        boolean failWhenMoreThanLimit = !testAccount2_3.deposit(limit + 1);
        boolean successWhenLessThanLimit = testAccount2_3.deposit(limit - 1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);

    }

    // ----- transfer ------ //
    @Test(expected = RuntimeException.class)
    @DisplayName("transfer limit mastercard")
    public final void TLM() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard", false);
        boolean failWhenMoreThanLimit = !testAccount2_1.transfer(limit + 1, testAccount1_1);
        boolean successWhenLessThanLimit = testAccount2_1.transfer(limit - 1, testAccount1_1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);
    }

    @Test(expected = RuntimeException.class)
    @DisplayName("transfer limit mastercard titanium")
    public final void TLMT() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard titanium", false);
        boolean failWhenMoreThanLimit = !testAccount2_2.transfer(limit + 1, testAccount1_1);
        boolean successWhenLessThanLimit = testAccount2_2.transfer(limit - 1, testAccount1_1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);

    }

    @Test(expected = RuntimeException.class)
    @DisplayName("transfer limit mastercard platinum")
    public final void TLMP() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard platinum", false);
        boolean failWhenMoreThanLimit = !testAccount2_3.transfer(limit + 1, testAccount1_1);
        boolean successWhenLessThanLimit = testAccount2_3.transfer(limit - 1, testAccount1_1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);
    }
    // ----- transfer to own ------ //

    @Test(expected = RuntimeException.class)
    @DisplayName("transfer to own limit mastercard")
    public final void TLMO() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard", true);
        boolean failWhenMoreThanLimit = !testAccount2_1.transfer(limit + 1, testAccount1_1);
        boolean successWhenLessThanLimit = testAccount2_1.transfer(limit - 1, testAccount1_1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);

    }

    @Test(expected = RuntimeException.class)
    @DisplayName("transfer to own limit mastercard titanium")
    public final void TLMTO() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard titanium", true);
        boolean failWhenMoreThanLimit = !testAccount2_2.transfer(limit + 1, testAccount1_1);
        boolean successWhenLessThanLimit = testAccount2_2.transfer(limit - 1, testAccount1_1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);

    }

    @Test(expected = RuntimeException.class)
    @DisplayName("transfer to own limit mastercard platinum")
    public final void TLMPO() throws IOException {
        double limit = testAccount.getCardTypeLimit("mastercard platinum", true);
        boolean failWhenMoreThanLimit = !testAccount2_3.transfer(limit + 1, testAccount1_1);
        boolean successWhenLessThanLimit = testAccount2_3.transfer(limit - 1, testAccount1_1);
        assertTrue(failWhenMoreThanLimit && successWhenLessThanLimit);

    }

}