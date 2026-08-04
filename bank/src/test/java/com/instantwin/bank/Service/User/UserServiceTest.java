package com.instantwin.bank.service.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.instantwin.bank.DTO.User.UserDTO;
import com.instantwin.bank.DTO.User.UserTransactionDTO;
import com.instantwin.bank.contract.Client.User.IUserTransactionClient;
import com.instantwin.bank.contract.Model.User.IUserFactory;
import com.instantwin.bank.model.User.UserEntity;
import com.instantwin.bank.repository.User.IUserRepository;
import com.instantwin.bank.service.User.UserService;
import com.instantwin.bank.utilities.User.TransactionRequestFailedException;

public class UserServiceTest {

    private static final long USER_ID = 1L;
    private static final long OTHER_USER_ID = 2L;

    private static final String FIRST_NAME = "Max";
    private static final String LAST_NAME = "Mustermann";
    private static final String OTHER_FIRST_NAME = "Erika";
    private static final String NEW_FIRST_NAME = "John";
    private static final String NEW_LAST_NAME = "Doe";

    private static final BigDecimal BALANCE = BigDecimal.valueOf(100);
    private static final BigDecimal OTHER_BALANCE = BigDecimal.valueOf(-50);
    private static final BigDecimal DEPOSIT_AMOUNT = BigDecimal.valueOf(50.75);
    private static final BigDecimal WITHDRAW_AMOUNT = BigDecimal.valueOf(30.25);

    private IUserRepository userRepository;
    private IUserFactory userFactory;
    private IUserTransactionClient transactionClient;
    private UserService userService;

    @BeforeEach
    void setUpService() {
        userRepository = mock(IUserRepository.class);
        userFactory = mock(IUserFactory.class);
        transactionClient = mock(IUserTransactionClient.class);
        userService = new UserService(userRepository, userFactory, transactionClient);

        when(transactionClient.depositTransaction(anyLong(), any(BigDecimal.class)))
                .thenReturn(ResponseEntity.ok("Deposit successful"));
        when(transactionClient.withdrawTransaction(anyLong(), any(BigDecimal.class)))
                .thenReturn(ResponseEntity.ok("Withdraw successful"));
    }

    @Test
    void testFindAllUsers_returns_all_users() {
        var firstUser = user(USER_ID, FIRST_NAME, LAST_NAME);
        var secondUser = user(OTHER_USER_ID, OTHER_FIRST_NAME, LAST_NAME);
        when(userRepository.findAll()).thenReturn(List.of(firstUser, secondUser));
        balanceFor(USER_ID, BALANCE);
        balanceFor(OTHER_USER_ID, OTHER_BALANCE);

        var result = userService.findAllUsers();

        assertEquals(2, result.size());
        assertUser(result.get(0), USER_ID, FIRST_NAME, LAST_NAME, BALANCE);
        assertUser(result.get(1), OTHER_USER_ID, OTHER_FIRST_NAME, LAST_NAME, OTHER_BALANCE);
    }

    @Test
    void testFindAllUsers_returns_empty_list_when_no_users() {
        when(userRepository.findAll()).thenReturn(List.of());

        var result = userService.findAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindUserById_returns_user_when_user_exists() {
        var entity = user(USER_ID, FIRST_NAME, LAST_NAME);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(entity));
        balanceFor(USER_ID, BALANCE);

        var result = userService.findUserById(USER_ID);

        assertTrue(result.isPresent());
        assertUser(result.get(), USER_ID, FIRST_NAME, LAST_NAME, BALANCE);
    }

    @Test
    void testFindUserById_returns_optional_empty_when_user_does_not_exist() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        var result = userService.findUserById(USER_ID);

        assertTrue(result.isEmpty());
        verify(transactionClient, never()).getAllTransactionsForUser(USER_ID);
    }

    @Test
    void testCreateUser_creates_user_with_given_names() {
        var createdUser = user(USER_ID, NEW_FIRST_NAME, NEW_LAST_NAME);
        var request = new UserDTO(NEW_FIRST_NAME, NEW_LAST_NAME);
        when(userFactory.createUser(NEW_FIRST_NAME, NEW_LAST_NAME)).thenReturn(createdUser);

        var result = userService.createUser(request);

        assertUser(result, USER_ID, NEW_FIRST_NAME, NEW_LAST_NAME, BigDecimal.ZERO);
        verify(userFactory).createUser(NEW_FIRST_NAME, NEW_LAST_NAME);
    }

    @Test
    void testCreateUser_creates_user_repository_save_is_called() {
        var createdUser = user(USER_ID, NEW_FIRST_NAME, NEW_LAST_NAME);
        var request = new UserDTO(NEW_FIRST_NAME, NEW_LAST_NAME);
        when(userFactory.createUser(NEW_FIRST_NAME, NEW_LAST_NAME)).thenReturn(createdUser);

        userService.createUser(request);

        verify(userRepository).save(createdUser);
    }

    @Test
    void testUpdateUserName_returns_optional_empty_when_user_does_not_exist() {
        var request = new UserDTO(NEW_FIRST_NAME, NEW_LAST_NAME);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        var result = userService.updateUserName(USER_ID, request);

        assertTrue(result.isEmpty());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUserName_updates_user_names_when_user_exists() {
        var entity = user(USER_ID, FIRST_NAME, LAST_NAME);
        var request = new UserDTO(NEW_FIRST_NAME, NEW_LAST_NAME);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(entity));
        balanceFor(USER_ID, BALANCE);

        var result = userService.updateUserName(USER_ID, request);

        assertTrue(result.isPresent());
        assertUser(result.get(), USER_ID, NEW_FIRST_NAME, NEW_LAST_NAME, BALANCE);
        verify(entity).changeFirstName(NEW_FIRST_NAME);
        verify(entity).changeLastName(NEW_LAST_NAME);
        verify(userRepository).save(entity);
    }

    @Test
    void testUpdateUserName_calculates_balance_before_saving_user() {
        var entity = user(USER_ID, FIRST_NAME, LAST_NAME);
        var request = new UserDTO(NEW_FIRST_NAME, NEW_LAST_NAME);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(entity));
        balanceFor(USER_ID, BALANCE);

        userService.updateUserName(USER_ID, request);

        var inOrder = inOrder(transactionClient, entity, userRepository);
        inOrder.verify(transactionClient).getAllTransactionsForUser(USER_ID);
        inOrder.verify(entity).changeFirstName(NEW_FIRST_NAME);
        inOrder.verify(entity).changeLastName(NEW_LAST_NAME);
        inOrder.verify(userRepository).save(entity);
    }

    @Test
    void testDeleteUser_returns_optional_empty_when_user_does_not_exist() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        var result = userService.deleteUser(USER_ID);

        assertTrue(result.isEmpty());
        verify(userRepository, never()).delete(any());
    }

    @Test
    void testDeleteUser_calculates_balance_before_deleting_user() {
        var entity = user(USER_ID, FIRST_NAME, LAST_NAME);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(entity));
        balanceFor(USER_ID, BALANCE);

        userService.deleteUser(USER_ID);

        var inOrder = inOrder(transactionClient, userRepository);
        inOrder.verify(transactionClient).getAllTransactionsForUser(USER_ID);
        inOrder.verify(userRepository).delete(entity);
    }

    @Test
    void testDeleteUser_returns_deleted_user_view() {
        var entity = user(USER_ID, FIRST_NAME, LAST_NAME);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(entity));
        balanceFor(USER_ID, BALANCE);

        var result = userService.deleteUser(USER_ID);

        assertTrue(result.isPresent());
        assertEquals(FIRST_NAME, result.get().getFirstName());
        assertEquals(LAST_NAME, result.get().getLastName());
        assertEquals(BALANCE, result.get().getBalance());
    }

    @Test
    void testDepositToUser_deposit_transaction_response_body_is_returned() {
        var response = ResponseEntity.ok("Deposit successful");
        when(transactionClient.depositTransaction(USER_ID, DEPOSIT_AMOUNT)).thenReturn(response);

        var result = userService.depositToUser(USER_ID, DEPOSIT_AMOUNT);

        assertTrue(result.isPresent());
        assertEquals("Deposit successful", result.get());
    }

    @Test
    void testDepositToUser_returns_optional_empty_when_user_does_not_exist() {
        when(transactionClient.depositTransaction(USER_ID, DEPOSIT_AMOUNT))
                .thenReturn(ResponseEntity.notFound().build());

        var result = userService.depositToUser(USER_ID, DEPOSIT_AMOUNT);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDepositToUser_throws_when_transaction_fails() {
        when(transactionClient.depositTransaction(USER_ID, DEPOSIT_AMOUNT))
                .thenReturn(ResponseEntity.internalServerError().build());

        assertThrows(TransactionRequestFailedException.class,
                () -> userService.depositToUser(USER_ID, DEPOSIT_AMOUNT));
    }

    @Test
    void testDepositToUser_deposit_transaction_is_delegated() {
        userService.depositToUser(USER_ID, DEPOSIT_AMOUNT);

        verify(transactionClient).depositTransaction(USER_ID, DEPOSIT_AMOUNT);
    }

    @Test
    void testDepositToUser_deposit_transaction_is_called_with_zero_amount() {
        userService.depositToUser(USER_ID, BigDecimal.ZERO);

        verify(transactionClient).depositTransaction(USER_ID, BigDecimal.ZERO);
    }

    @Test
    void testWithdrawFromUser_withdraw_transaction_response_body_is_returned() {
        var response = ResponseEntity.ok("Withdraw successful");
        when(transactionClient.withdrawTransaction(USER_ID, WITHDRAW_AMOUNT)).thenReturn(response);

        var result = userService.withdrawFromUser(USER_ID, WITHDRAW_AMOUNT);

        assertTrue(result.isPresent());
        assertEquals("Withdraw successful", result.get());
    }

    @Test
    void testWithdrawFromUser_returns_optional_empty_when_user_does_not_exist() {
        when(transactionClient.withdrawTransaction(USER_ID, WITHDRAW_AMOUNT))
                .thenReturn(ResponseEntity.notFound().build());

        var result = userService.withdrawFromUser(USER_ID, WITHDRAW_AMOUNT);

        assertTrue(result.isEmpty());
    }

    @Test
    void testWithdrawFromUser_throws_when_transaction_fails() {
        when(transactionClient.withdrawTransaction(USER_ID, WITHDRAW_AMOUNT))
                .thenReturn(ResponseEntity.internalServerError().build());

        assertThrows(TransactionRequestFailedException.class,
                () -> userService.withdrawFromUser(USER_ID, WITHDRAW_AMOUNT));
    }

    @Test
    void testWithdrawFromUser_withdraw_transaction_is_delegated() {
        userService.withdrawFromUser(USER_ID, WITHDRAW_AMOUNT);

        verify(transactionClient).withdrawTransaction(USER_ID, WITHDRAW_AMOUNT);
    }

    @Test
    void testWithdrawFromUser_withdraw_transaction_is_called_with_zero_amount() {
        userService.withdrawFromUser(USER_ID, BigDecimal.ZERO);

        verify(transactionClient).withdrawTransaction(USER_ID, BigDecimal.ZERO);
    }

    @Test
    void testCheckIfUserExists_returns_optional_empty_when_user_does_not_exist() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        var result = userService.checkIfUserExists(USER_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCheckIfUserExists_returns_user_exists_view_when_user_exists() {
        var entity = user(USER_ID, FIRST_NAME, LAST_NAME);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(entity));

        var result = userService.checkIfUserExists(USER_ID);

        assertTrue(result.isPresent());
        assertEquals(FIRST_NAME, result.get().getFirstName());
        assertEquals(LAST_NAME, result.get().getLastName());
    }

    private void balanceFor(long userId, BigDecimal balance) {
        when(transactionClient.getAllTransactionsForUser(userId))
                .thenReturn(Optional.of(List.of(new UserTransactionDTO(balance))));
    }

    private UserEntity user(long id, String firstName, String lastName) {
        var entity = mock(UserEntity.class);
        var currentFirstName = new AtomicReference<>(firstName);
        var currentLastName = new AtomicReference<>(lastName);

        when(entity.getId()).thenReturn(id);
        when(entity.getFirstName()).thenAnswer(ignored -> currentFirstName.get());
        when(entity.getLastName()).thenAnswer(ignored -> currentLastName.get());

        doAnswer(invocation -> {
            currentFirstName.set(invocation.getArgument(0));
            return null;
        }).when(entity).changeFirstName(any(String.class));
        doAnswer(invocation -> {
            currentLastName.set(invocation.getArgument(0));
            return null;
        }).when(entity).changeLastName(any(String.class));

        return entity;
    }

    private void assertUser(
            com.instantwin.bank.contract.View.User.IUserView actual,
            long expectedId,
            String expectedFirstName,
            String expectedLastName,
            BigDecimal expectedBalance) {
        assertEquals(expectedId, actual.getId());
        assertEquals(expectedFirstName, actual.getFirstName());
        assertEquals(expectedLastName, actual.getLastName());
        assertEquals(expectedBalance, actual.getBalance());
    }
}
