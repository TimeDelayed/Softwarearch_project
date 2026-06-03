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
import com.instantwin.bank.Utilities.InsufficientBalanceException;
import com.instantwin.bank.Utilities.TransactionNumberInvalidException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserEntityTest {

    private static long SUITE_SEED;
    private Random random;

    private String firstNameString = "Max";
    private String lastNameString = "Mustermann";
    private UserEntity user;

    @BeforeAll
    void initSuiteSeed() {
        String raw = System.getProperty("test.seed");

        SUITE_SEED = (raw == null || raw.isBlank())
                ? System.nanoTime()
                : Long.parseLong(raw);
    }

    @BeforeEach
    void initUser() {
        user = UserEntity.of(firstNameString, lastNameString);
    }

    @BeforeEach
    void initRandom(TestInfo testInfo) {
        long testSeed = SUITE_SEED ^ testInfo.getDisplayName().hashCode();
        random = new Random(testSeed);
    }

    @AfterAll
    static void printSeed() {
        System.out.println("Seed used: " + SUITE_SEED);
    }

    @Test
    void testCreate_firstName_not_null() {
        assertThrows(ModelValidityBreachException.class,
                () -> UserEntity.of(null, lastNameString));
    }

    @Test
    void testCreate_firstName_not_empty() {
        assertThrows(ModelValidityBreachException.class,
                () -> UserEntity.of("  ", lastNameString));
    }

    @Test
    void testCreate_lastName_not_null() {
        assertThrows(ModelValidityBreachException.class,
                () -> UserEntity.of(firstNameString, null));
    }

    @Test
    void testCreate_lastName_not_empty() {
        assertThrows(ModelValidityBreachException.class,
                () -> UserEntity.of(firstNameString, "  "));
    }

    @Test
    void testCreate_makes_correct_names() {
        assertEquals(firstNameString, user.getFirstName());
        assertEquals(lastNameString, user.getLastName());
    }

    @Test
    void testDeposit_amount_not_null() {
        assertThrows(TransactionNumberInvalidException.class,
                () -> user.deposit(null));
    }

    @Test
    void testDeposit_amount_not_negative() {
        Float amount = -(0.01f + random.nextFloat() * 1_000f);

        assertThrows(TransactionNumberInvalidException.class,
                () -> user.deposit(new BigDecimal(amount.toString())));
    }

    @Test
    void testDeposit_valid() {
        Float amount = 0.01f + random.nextFloat() * 1_000f;
        BigDecimal value = new BigDecimal(amount.toString());

        user.deposit(value);

        assertEquals(value, user.getBalance());
    }

    @Test
    void testDeposit_add_is_right_amount() {
        Float amount = 0.01f + random.nextFloat() * 1_000f;
        BigDecimal value = new BigDecimal(amount.toString());

        user.deposit(value);
        assertEquals(value, user.getBalance());

        user.deposit(value);
        assertEquals(value.add(value), user.getBalance());
    }

    @Test
    void testWithdraw_amount_not_null() {
        assertThrows(TransactionNumberInvalidException.class,
                () -> user.withdraw(null));
    }

    @Test
    void testWithdraw_amount_not_negative() {
        Float amount = -(0.01f + random.nextFloat() * 1_000f);

        assertThrows(TransactionNumberInvalidException.class,
                () -> user.withdraw(new BigDecimal(amount.toString())));
    }

    @Test
    void testWithdraw_amount_not_bigger_than_balance() {
        Float deposit = 0.01f + random.nextFloat() * 1_000f;
        Float withdraw = deposit + (0.01f + random.nextFloat() * 1_000f);

        user.deposit(new BigDecimal(deposit.toString()));

        assertThrows(InsufficientBalanceException.class,
                () -> user.withdraw(new BigDecimal(withdraw.toString())));
    }

    @Test
    void testWithdraw_valid_smaller_than_balance() {
        Float deposit = 0.01f + random.nextFloat() * 1_000f;
        Float withdraw = deposit * random.nextFloat();

        BigDecimal depositValue = new BigDecimal(deposit.toString());
        BigDecimal withdrawValue = new BigDecimal(withdraw.toString());
        BigDecimal expected = depositValue.subtract(withdrawValue);

        user.deposit(depositValue);

        assertDoesNotThrow(() -> user.withdraw(withdrawValue));

        assertEquals(expected, user.getBalance());
    }

    @Test
    void testWithdraw_valid_same_as_balance() {
        Float deposit = 0.01f + random.nextFloat() * 1_000f;

        BigDecimal value = new BigDecimal(deposit.toString());

        user.deposit(value);

        assertDoesNotThrow(() -> user.withdraw(value));

        assertTrue(user.getBalance().compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    void testWithdraw_subtracts_right_amount() {
        Float deposit = 0.01f + random.nextFloat() * 1_000f;
        Float withdraw = deposit * random.nextFloat() / 2;

        BigDecimal depositValue = new BigDecimal(deposit.toString());
        BigDecimal withdrawValue = new BigDecimal(withdraw.toString());

        BigDecimal firstExpected = depositValue.subtract(withdrawValue);
        BigDecimal secondExpected = firstExpected.subtract(withdrawValue);

        user.deposit(depositValue);

        assertDoesNotThrow(() -> user.withdraw(withdrawValue));
        assertEquals(firstExpected, user.getBalance());

        assertDoesNotThrow(() -> user.withdraw(withdrawValue));
        assertEquals(secondExpected, user.getBalance());
    }

    @Test
    void testChangeFirstName_first_name_not_null() {
        assertThrows(ModelValidityBreachException.class,
                () -> user.changeFirstName(null));
    }

    @Test
    void testChangeFirstName_first_name_not_empty() {
        assertThrows(ModelValidityBreachException.class,
                () -> user.changeFirstName(" "));
    }

    @Test
    void testChangeFirstName_valid() {
        String newFirstName = firstNameString + random.nextInt(1_000);

        user.changeFirstName(newFirstName);

        assertEquals(newFirstName, user.getFirstName());
    }

    @Test
    void testChangeLastName_last_name_not_null() {
        assertThrows(ModelValidityBreachException.class,
                () -> user.changeLastName(null));
    }

    @Test
    void testChangeLastName_last_name_not_empty() {
        assertThrows(ModelValidityBreachException.class,
                () -> user.changeLastName(" "));
    }

    @Test
    void testChangeLastName_valid() {
        String newLastName = lastNameString + random.nextInt(1_000);

        user.changeLastName(newLastName);

        assertEquals(newLastName, user.getLastName());
    }
}