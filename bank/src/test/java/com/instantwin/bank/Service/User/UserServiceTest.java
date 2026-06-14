package com.instantwin.bank.Service.User;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.http.ResponseEntity;

import com.instantwin.bank.DTO.User.UserRequestTransaction;
import com.instantwin.bank.Model.User.UserEntity;
import com.instantwin.bank.Repository.User.IUserRepository;
import com.instantwin.bank.Utilities.User.UserInvoicingParty;
import com.instantwin.bank.contract.Client.IUserTransactionClient;
import com.instantwin.bank.contract.Model.User.IUserFactory;
import com.instantwin.bank.DTO.User.UserDTO;

public class UserServiceTest {
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

        when(user1.getId()).thenReturn(1L);
        when(user2.getId()).thenReturn(2L);

        when(user1.getFirstName()).thenReturn("Max");
        when(user1.getLastName()).thenReturn("Mustermann");

        when(user2.getFirstName()).thenReturn("Erika");
        when(user2.getLastName()).thenReturn("Mustermann");

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
        UserRequestTransaction transaction1 = new UserRequestTransaction(UserInvoicingParty.USER_SLICE,
                BigDecimal.valueOf(100));
        UserRequestTransaction transaction2 = new UserRequestTransaction(UserInvoicingParty.USER_SLICE,
                BigDecimal.valueOf(-50));

        when(transactionClient.getAllTransactionsForUser(1L)).thenReturn(Optional.of(List.of(transaction1)));
        when(transactionClient.getAllTransactionsForUser(2L)).thenReturn(Optional.of(List.of(transaction2)));

        var result = userHandler.findAllUsers();

        assertEquals(2, result.size());
        assertEquals("Max", result.get(0).getFirstName());
        assertEquals("Mustermann", result.get(0).getLastName());
        assertEquals("Erika", result.get(1).getFirstName());
        assertEquals("Mustermann", result.get(1).getLastName());
        assertEquals(BigDecimal.valueOf(100), result.get(0).getBalance());
        assertEquals(BigDecimal.valueOf(-50), result.get(1).getBalance());
    }

    @Test
    void testFindAllUsers_returns_empty_list_when_no_users() {
        when(userRepository.findAll()).thenReturn(List.of());

        var result = userHandler.findAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindUserById_returns_user_when_user_exists() {
        UserRequestTransaction transaction1 = new UserRequestTransaction(UserInvoicingParty.USER_SLICE,
                BigDecimal.valueOf(100));

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntities.get(0)));
        when(transactionClient.getAllTransactionsForUser(1L)).thenReturn(Optional.of(List.of(transaction1)));

        var result = userHandler.findUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("Max", result.get().getFirstName());
        assertEquals("Mustermann", result.get().getLastName());
        assertEquals(BigDecimal.valueOf(100), result.get().getBalance());
    }

    @Test
    void testFindUserById_returns_optional_empty_when_user_does_not_exist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        var result = userHandler.findUserById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateUser_creates_user_with_given_names() {
        var userDTO = new UserDTO("John", "Doe");
        var result = userHandler.createUser(userDTO);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void testCreateUser_creates_user_repository_save_is_called() {
        var userDTO = new UserDTO("John", "Doe");

        var result = userHandler.createUser(userDTO);

        verify(userRepository).save(any(UserEntity.class));
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void testUpdateUserName_returns_optional_empty_when_user_does_not_exist() {
        var userDTO = new UserDTO("John", "Doe");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        var result = userHandler.updateUserName(1L, userDTO);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateUserName_updates_user_names_when_user_exists() {
        var userDTO = new UserDTO("John", "Doe");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntities.get(0)));

        var result = userHandler.updateUserName(1L, userDTO);

        verify(userRepository).save(any(UserEntity.class));
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());
    }

    @Test
    void testDeleteUser_returns_optional_empty_when_user_does_not_exist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        var result = userHandler.deleteUser(1L);

        assertTrue(result.isEmpty());
        verify(userRepository, never()).delete(any(UserEntity.class));
    }

    @Test
    void testDeleteUser_calculates_balance_before_deleting_user() {
        var transaction1 = new UserRequestTransaction(
                UserInvoicingParty.USER_SLICE,
                BigDecimal.valueOf(100));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(userEntities.get(0)));

        when(transactionClient.getAllTransactionsForUser(1L))
                .thenReturn(Optional.of(List.of(transaction1)));

        userHandler.deleteUser(1L);

        InOrder inOrder = inOrder(transactionClient, userRepository);

        inOrder.verify(transactionClient)
                .getAllTransactionsForUser(1L);

        inOrder.verify(userRepository)
                .delete(any(UserEntity.class));
    }

    @Test
    void testDeleteUser_returns_deleted_user_view() {
        var transaction1 = new UserRequestTransaction(
                UserInvoicingParty.USER_SLICE,
                BigDecimal.valueOf(100));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(userEntities.get(0)));

        when(transactionClient.getAllTransactionsForUser(1L))
                .thenReturn(Optional.of(List.of(transaction1)));

        var result = userHandler.deleteUser(1L);

        assertTrue(result.isPresent());
        assertEquals("Max", result.get().getFirstName());
        assertEquals("Mustermann", result.get().getLastName());
        assertEquals(BigDecimal.valueOf(100), result.get().getBalance());
    }

    @Test
    void testDepositToUser_deposit_transaction_response_is_returned() {
        when(transactionClient.depositTransaction(1L, BigDecimal.valueOf(50)))
                .thenReturn(ResponseEntity.ok("Deposit successful"));

        var result = userHandler.depositToUser(1L, BigDecimal.valueOf(50));

        assertEquals(ResponseEntity.ok("Deposit successful"), result);
    }

    @Test
    void testDepositToUser_deposit_transaction_is_delegated() {
        userHandler.depositToUser(1L, BigDecimal.valueOf(50.75));

        verify(transactionClient).depositTransaction(1L, BigDecimal.valueOf(50.75));
    }

    @Test
    void testDepositToUser_deposit_transaction_is_called_with_zero_amount() {
        userHandler.depositToUser(1L, BigDecimal.ZERO);

        verify(transactionClient).depositTransaction(1L, BigDecimal.ZERO);
    }

    @Test
    void testWithdrawFromUser_withdraw_transaction_response_is_returned() {
        when(transactionClient.withdrawTransaction(1L, BigDecimal.valueOf(50)))
                .thenReturn(ResponseEntity.ok("Withdraw successful"));

        var result = userHandler.withdrawFromUser(1L, BigDecimal.valueOf(50));

        assertEquals(ResponseEntity.ok("Withdraw successful"), result);
    }

    @Test
    void testWithdrawFromUser_withdraw_transaction_is_delegated() {
        userHandler.withdrawFromUser(1L, BigDecimal.valueOf(30.25));

        verify(transactionClient).withdrawTransaction(1L, BigDecimal.valueOf(30.25));
    }

    @Test
    void testWithdrawFromUser_withdraw_transaction_is_called_with_zero_amount() {
        userHandler.withdrawFromUser(1L, BigDecimal.ZERO);

        verify(transactionClient).withdrawTransaction(1L, BigDecimal.ZERO);
    }
}
