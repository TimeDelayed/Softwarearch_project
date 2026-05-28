package com.instantwin.bank.Model.Entity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;

import com.instantwin.bank.Model.User.UserEntity;
import com.instantwin.bank.Utilities.ModelValidityBreachException;
import com.instantwin.bank.Utilities.TransactionNumberInvalidException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserEntityTest {
    private static long SUITE_SEED;
    private Random random;

    // Testing variables
    private String firstNameString = "Max";
    private String lastNameString = "Mustermann";

    @BeforeAll
    void initSuiteSeed(){

        String raw = System.getProperty("test.seed");

        SUITE_SEED = (raw == null || raw.isBlank())
                ? System.nanoTime()
                : Long.parseLong(raw);
    }

    @BeforeEach
    void initRandom(TestInfo testInfo){

        long testSeed = SUITE_SEED ^ testInfo.getDisplayName().hashCode();
        random = new Random(testSeed);
    }

    @AfterAll
    static void printSeed(){

        System.out.println("Seed used: " + SUITE_SEED);
    }

    @Test
    void testCreate_firstName_not_null() {
        assertThrows(ModelValidityBreachException.class, 
            () -> UserEntity.of(null, lastNameString));
    }

    @Test
    void testCreate_lastName_not_null() {
        assertThrows(ModelValidityBreachException.class, 
            () -> UserEntity.of(firstNameString, null));
    }

    @Test
    void testCreate_makes_correct_names() {
        UserEntity u = UserEntity.of(firstNameString, lastNameString);
        assertEquals(firstNameString, u.getFirstName());
        assertEquals(lastNameString, u.getLastName());
    }

    @Test
    void testDeposit_amount_not_null() {
        UserEntity u = UserEntity.of(firstNameString, lastNameString);
        assertThrows(TransactionNumberInvalidException.class,() -> u.deposit(null));
    }

    @Test
    void testDeposit_amount_not_negative() {
        Float amount = -(0.01f + random.nextFloat() * 1_000f);
        assertFalse(u.deposit(new BigDecimal(amount.toString())));
    }

    @Test
    void testDeposit_amount_not_bigger_than_max_float() {       

        BigDecimal tooLarge = BigDecimal.valueOf(Float.MAX_VALUE)
                                        .add(new BigDecimal(Float.toString(random.nextFloat())));

        assertFalse(u.deposit(tooLarge));
    }

    @Test
    void testDeposit_valid() {
        Float amount = 0.01f + random.nextFloat() * 1_000f;
        assertTrue(u.deposit(new BigDecimal(amount.toString())));
    }

    @Test
    void testDeposit_add_is_right_amount() {
        Float amount = 0.01f + random.nextFloat() * 1_000f;

        u.deposit(new BigDecimal(amount.toString()));
        assertEquals(amount, u.getBalance());

        u.deposit(new BigDecimal(amount.toString()));
        assertEquals(amount+amount, u.getBalance());
    }

    @Test
    void testWithdraw_amount_not_null() {
        assertFalse(u.withdraw(null));
    }

    @Test
    void testWithdraw_amount_not_negative() {
        Float amount = -(0.01f + random.nextFloat() * 1_000f);

        assertFalse(u.withdraw(new BigDecimal(amount.toString())));
    }

    @Test
    void testWithdraw_amount_not_bigger_than_balance() {

        Float deposit = 100f;
        Float withdraw = deposit + (0.01f + random.nextFloat() * 1_000f);

        u.deposit(new BigDecimal(deposit.toString()));

        assertFalse(u.withdraw(new BigDecimal(withdraw.toString())));
    }

    @Test
    void testWithdraw_valid() {

        Float deposit = 1_000f;
        Float withdraw = 0.01f + random.nextFloat() * 500f;

        u.deposit(new BigDecimal(deposit.toString()));

        assertTrue(u.withdraw(new BigDecimal(withdraw.toString())));
    }

    @Test
    void testWithdraw_subtracts_right_amount() {

        Float deposit = 1_000f;
        Float withdraw = 0.01f + random.nextFloat() * 500f;

        u.deposit(new BigDecimal(deposit.toString()));

        u.withdraw(new BigDecimal(withdraw.toString()));

        assertEquals(deposit - withdraw, u.getBalance());

        u.withdraw(new BigDecimal(withdraw.toString()));

        assertEquals(deposit - withdraw - withdraw, u.getBalance());
    }

    @Test
    void testChangeFirstName_first_name_not_null() {
        assertFalse(u.changeFirstName(null));
    }

    @Test
    void testChangeFirstName_valid() {

        String newFirstName = firstNameString + random.nextInt(1_000);

        assertTrue(u.changeFirstName(newFirstName));
    }

    @Test
    void testChangeFirstName_changes_right_value() {

        String newFirstName =
                firstNameString + random.nextInt(1_000);

        u.changeFirstName(newFirstName);

        assertEquals(newFirstName, u.getFirstName());
    }

    @Test
    void testChangeLastName_last_name_not_null() {
        assertFalse(u.changeLastName(null));
    }

    @Test
    void testChangeLastName_valid() {

        String newLastName =
                lastNameString + random.nextInt(1_000);

        assertTrue(u.changeLastName(newLastName));
    }

    @Test
    void testChangeLastName_changes_right_value() {

        String newLastName =
                lastNameString + random.nextInt(1_000);

        u.changeLastName(newLastName);

        assertEquals(newLastName, u.getLastName());
    }
}
