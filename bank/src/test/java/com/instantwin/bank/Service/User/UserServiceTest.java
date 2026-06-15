package com.instantwin.bank.Service.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.http.ResponseEntity;

import com.instantwin.bank.DTO.User.UserDTO;
import com.instantwin.bank.DTO.User.UserTransactionDTO;
import com.instantwin.bank.Model.User.UserEntity;
import com.instantwin.bank.Repository.User.IUserRepository;
import com.instantwin.bank.contract.Client.User.IUserTransactionClient;
import com.instantwin.bank.contract.Model.User.IUserFactory;

public class UserServiceTest {

    private static final long USER_1_ID = 1L;
    private static final long USER_2_ID = 2L;

    private static final int FIRST_USER_INDEX = 0;
    private static final int SECOND_USER_INDEX = 1;
    private static final int USER_COUNT = 2;

    private static final String USER_1_FIRST_NAME = "Max";
    private static final String USER_1_LAST_NAME = "Mustermann";
    private static final String USER_2_FIRST_NAME = "Erika";
    private static final String USER_2_LAST_NAME = "Mustermann";

    private static final String NEW_USER_FIRST_NAME = "John";
    private static final String NEW_USER_LAST_NAME = "Doe";

    private static final BigDecimal TRANSACTION_AMOUNT_100 = BigDecimal.valueOf(100);
    private static final BigDecimal TRANSACTION_AMOUNT_MINUS_50 = BigDecimal.valueOf(-50);
    private static final BigDecimal DEPOSIT_AMOUNT_50 = BigDecimal.valueOf(50);
    private static final BigDecimal DEPOSIT_AMOUNT_50_75 = BigDecimal.valueOf(50.75);
    private static final BigDecimal WITHDRAW_AMOUNT_50 = BigDecimal.valueOf(50);
    private static final BigDecimal WITHDRAW_AMOUNT_30_25 = BigDecimal.valueOf(30.25);

    private static final String DEPOSIT_SUCCESSFUL_RESPONSE = "Deposit successful";
    private static final String WITHDRAW_SUCCESSFUL_RESPONSE = "Withdraw successful";

    private IUserRepository userRepository;
    private IUserFactory userFactory;
    private IUserTransactionClient transactionClient;
    private UserService userHandler;
    private List<UserEntity> userEntities;

    @BeforeEach
    void setup() {
        this.userRepository = mock(IUserRepository.class);
        this.userFactory = mock(IUserFactory.class);
        this.transactionClient = mock(IUserTransactionClient.class);
        this.userHandler = new UserService(userRepository, userFactory, transactionClient);

        var user1 = mock(UserEntity.class);
        var user2 = mock(UserEntity.class);

        when(user1.getId()).thenReturn(USER_1_ID);
        when(user2.getId()).thenReturn(USER_2_ID);

        when(user1.getFirstName()).thenReturn(USER_1_FIRST_NAME);
        when(user1.getLastName()).thenReturn(USER_1_LAST_NAME);

        when(user2.getFirstName()).thenReturn(USER_2_FIRST_NAME);
        when(user2.getLastName()).thenReturn(USER_2_LAST_NAME);

        doAnswer(invocation -> {
            String newFirstName = invocation.getArgument(0);
            when(user1.getFirstName()).thenReturn(newFirstName);
            return null;
        }).when(user1).changeFirstName(anyString());

        doAnswer(invocation -> {
            String newLastName = invocation.getArgument(0);
            when(user1.getLastName()).thenReturn(newLastName);
            return null;
        }).when(user1).changeLastName(anyString());

        doAnswer(invocation -> {
            String newFirstName = invocation.getArgument(0);
            when(user2.getFirstName()).thenReturn(newFirstName);
            return null;
        }).when(user2).changeFirstName(anyString());

        doAnswer(invocation -> {
            String newLastName = invocation.getArgument(0);
            when(user2.getLastName()).thenReturn(newLastName);
            return null;
        }).when(user2).changeLastName(anyString());

        this.userEntities = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(this.userEntities);

        when(userFactory.createUser(anyString(), anyString())).thenAnswer(invocation -> {
            String firstName = invocation.getArgument(0);
            String lastName = invocation.getArgument(1);
            var user = mock(UserEntity.class);

            when(user.getFirstName()).thenReturn(firstName);
            when(user.getLastName()).thenReturn(lastName);

            return user;
        });
    }

    @Test
    void testFindAllUsers_returns_all_users() {
        UserTransactionDTO transaction1 = new UserTransactionDTO(TRANSACTION_AMOUNT_100);
        UserTransactionDTO transaction2 = new UserTransactionDTO(TRANSACTION_AMOUNT_MINUS_50);

        when(transactionClient.getAllTransactionsForUser(USER_1_ID))
                .thenReturn(Optional.of(List.of(transaction1)));

        when(transactionClient.getAllTransactionsForUser(USER_2_ID))
                .thenReturn(Optional.of(List.of(transaction2)));

        var result = userHandler.findAllUsers();

        assertEquals(USER_COUNT, result.size());
        assertEquals(USER_1_FIRST_NAME, result.get(FIRST_USER_INDEX).getFirstName());
        assertEquals(USER_1_LAST_NAME, result.get(FIRST_USER_INDEX).getLastName());
        assertEquals(USER_2_FIRST_NAME, result.get(SECOND_USER_INDEX).getFirstName());
        assertEquals(USER_2_LAST_NAME, result.get(SECOND_USER_INDEX).getLastName());
        assertEquals(TRANSACTION_AMOUNT_100, result.get(FIRST_USER_INDEX).getBalance());
        assertEquals(TRANSACTION_AMOUNT_MINUS_50, result.get(SECOND_USER_INDEX).getBalance());
    }

    @Test
    void testFindAllUsers_returns_empty_list_when_no_users() {
        when(userRepository.findAll()).thenReturn(List.of());

        var result = userHandler.findAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindUserById_returns_user_when_user_exists() {
        UserTransactionDTO transaction1 = new UserTransactionDTO(TRANSACTION_AMOUNT_100);

        when(userRepository.findById(USER_1_ID))
                .thenReturn(Optional.of(userEntities.get(FIRST_USER_INDEX)));

        when(transactionClient.getAllTransactionsForUser(USER_1_ID))
                .thenReturn(Optional.of(List.of(transaction1)));

        var result = userHandler.findUserById(USER_1_ID);

        assertTrue(result.isPresent());
        assertEquals(USER_1_FIRST_NAME, result.get().getFirstName());
        assertEquals(USER_1_LAST_NAME, result.get().getLastName());
        assertEquals(TRANSACTION_AMOUNT_100, result.get().getBalance());
    }

    @Test
    void testFindUserById_returns_optional_empty_when_user_does_not_exist() {
        when(userRepository.findById(USER_1_ID)).thenReturn(Optional.empty());

        var result = userHandler.findUserById(USER_1_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateUser_creates_user_with_given_names() {
        var userDTO = new UserDTO(NEW_USER_FIRST_NAME, NEW_USER_LAST_NAME);

        var result = userHandler.createUser(userDTO);

        assertEquals(NEW_USER_FIRST_NAME, result.getFirstName());
        assertEquals(NEW_USER_LAST_NAME, result.getLastName());
    }

    @Test
    void testCreateUser_creates_user_repository_save_is_called() {
        var userDTO = new UserDTO(NEW_USER_FIRST_NAME, NEW_USER_LAST_NAME);

        var result = userHandler.createUser(userDTO);

        verify(userRepository).save(any(UserEntity.class));
        assertEquals(NEW_USER_FIRST_NAME, result.getFirstName());
        assertEquals(NEW_USER_LAST_NAME, result.getLastName());
    }

    @Test
    void testUpdateUserName_returns_optional_empty_when_user_does_not_exist() {
        var userDTO = new UserDTO(NEW_USER_FIRST_NAME, NEW_USER_LAST_NAME);

        when(userRepository.findById(USER_1_ID)).thenReturn(Optional.empty());

        var result = userHandler.updateUserName(USER_1_ID, userDTO);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateUserName_updates_user_names_when_user_exists() {
        var userDTO = new UserDTO(NEW_USER_FIRST_NAME, NEW_USER_LAST_NAME);

        when(userRepository.findById(USER_1_ID))
                .thenReturn(Optional.of(userEntities.get(FIRST_USER_INDEX)));

        var result = userHandler.updateUserName(USER_1_ID, userDTO);

        verify(userRepository).save(any(UserEntity.class));
        assertTrue(result.isPresent());
        assertEquals(NEW_USER_FIRST_NAME, result.get().getFirstName());
        assertEquals(NEW_USER_LAST_NAME, result.get().getLastName());
    }

    @Test
    void testDeleteUser_returns_optional_empty_when_user_does_not_exist() {
        when(userRepository.findById(USER_1_ID)).thenReturn(Optional.empty());

        var result = userHandler.deleteUser(USER_1_ID);

        assertTrue(result.isEmpty());
        verify(userRepository, never()).delete(any(UserEntity.class));
    }

    @Test
    void testDeleteUser_calculates_balance_before_deleting_user() {
        UserTransactionDTO transaction1 = new UserTransactionDTO(TRANSACTION_AMOUNT_100);

        when(userRepository.findById(USER_1_ID))
                .thenReturn(Optional.of(userEntities.get(FIRST_USER_INDEX)));

        when(transactionClient.getAllTransactionsForUser(USER_1_ID))
                .thenReturn(Optional.of(List.of(transaction1)));

        userHandler.deleteUser(USER_1_ID);

        InOrder inOrder = inOrder(transactionClient, userRepository);

        inOrder.verify(transactionClient)
                .getAllTransactionsForUser(USER_1_ID);

        inOrder.verify(userRepository)
                .delete(any(UserEntity.class));
    }

    @Test
    void testDeleteUser_returns_deleted_user_view() {
        UserTransactionDTO transaction1 = new UserTransactionDTO(TRANSACTION_AMOUNT_100);

        when(userRepository.findById(USER_1_ID))
                .thenReturn(Optional.of(userEntities.get(FIRST_USER_INDEX)));

        when(transactionClient.getAllTransactionsForUser(USER_1_ID))
                .thenReturn(Optional.of(List.of(transaction1)));

        var result = userHandler.deleteUser(USER_1_ID);

        assertTrue(result.isPresent());
        assertEquals(USER_1_FIRST_NAME, result.get().getFirstName());
        assertEquals(USER_1_LAST_NAME, result.get().getLastName());
        assertEquals(TRANSACTION_AMOUNT_100, result.get().getBalance());
    }

    @Test
    void testDepositToUser_deposit_transaction_response_is_returned() {
        when(transactionClient.depositTransaction(USER_1_ID, DEPOSIT_AMOUNT_50))
                .thenReturn(ResponseEntity.ok(DEPOSIT_SUCCESSFUL_RESPONSE));

        var result = userHandler.depositToUser(USER_1_ID, DEPOSIT_AMOUNT_50);

        assertEquals(ResponseEntity.ok(DEPOSIT_SUCCESSFUL_RESPONSE), result);
    }

    @Test
    void testDepositToUser_deposit_transaction_is_delegated() {
        userHandler.depositToUser(USER_1_ID, DEPOSIT_AMOUNT_50_75);

        verify(transactionClient).depositTransaction(USER_1_ID, DEPOSIT_AMOUNT_50_75);
    }

    @Test
    void testDepositToUser_deposit_transaction_is_called_with_zero_amount() {
        userHandler.depositToUser(USER_1_ID, BigDecimal.ZERO);

        verify(transactionClient).depositTransaction(USER_1_ID, BigDecimal.ZERO);
    }

    @Test
    void testWithdrawFromUser_withdraw_transaction_response_is_returned() {
        when(transactionClient.withdrawTransaction(USER_1_ID, WITHDRAW_AMOUNT_50))
                .thenReturn(ResponseEntity.ok(WITHDRAW_SUCCESSFUL_RESPONSE));

        var result = userHandler.withdrawFromUser(USER_1_ID, WITHDRAW_AMOUNT_50);

        assertEquals(ResponseEntity.ok(WITHDRAW_SUCCESSFUL_RESPONSE), result);
    }

    @Test
    void testWithdrawFromUser_withdraw_transaction_is_delegated() {
        userHandler.withdrawFromUser(USER_1_ID, WITHDRAW_AMOUNT_30_25);

        verify(transactionClient).withdrawTransaction(USER_1_ID, WITHDRAW_AMOUNT_30_25);
    }

    @Test
    void testWithdrawFromUser_withdraw_transaction_is_called_with_zero_amount() {
        userHandler.withdrawFromUser(USER_1_ID, BigDecimal.ZERO);

        verify(transactionClient).withdrawTransaction(USER_1_ID, BigDecimal.ZERO);
    }
}