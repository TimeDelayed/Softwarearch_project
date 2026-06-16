package com.instantwin.bank.Model.User;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import com.instantwin.bank.Utilities.User.ModelValidityBreachException;

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