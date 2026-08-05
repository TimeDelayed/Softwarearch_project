package com.instantwin.bank.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.instantwin.bank.DTO.User.UserDTO;
import com.instantwin.bank.contract.Service.User.IUserService;
import com.instantwin.bank.contract.View.User.IUserDeleteView;
import com.instantwin.bank.contract.View.User.IUserView;
import com.instantwin.bank.controller.User.UserController;
import com.instantwin.bank.model.User.UserEntity;
import com.instantwin.bank.utilities.User.DecimalPlaceInvalidException;
import com.instantwin.bank.view.User.UserExistsView;
import com.instantwin.bank.view.User.UserView;

public class UserControllerTest {

    private UserController userController;
    private IUserService userHandler;
    private UserEntity userEntity;
    private IUserView userView;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {

        this.userHandler = mock(IUserService.class);
        this.userController = new UserController(userHandler);

        this.userEntity = mock(UserEntity.class);

        when(userEntity.getId()).thenReturn(1L);
        when(userEntity.getFirstName()).thenReturn("Max");
        when(userEntity.getLastName()).thenReturn("Mustermann");

        this.userView = UserView.of(userEntity, new BigDecimal("100.00"));

        this.userDTO = new UserDTO("Max", "Mustermann");

        when(userHandler.createUser(userDTO)).thenReturn(userView);
        when(userHandler.depositToUser(anyLong(), any())).thenReturn(Optional.of("Deposit successful"));
        when(userHandler.withdrawFromUser(anyLong(), any())).thenReturn(Optional.of("Withdraw successful"));
    }

    @Test
    void testCreateUser_returns_created_201() {
        UserDTO userDTO = new UserDTO("Max", "Mustermann");

        var result = userController.createUser(userDTO);

        assertEquals(201, result.getStatusCode().value());
        assertTrue(result.getBody() instanceof IUserView);
        assertEquals("Max", result.getBody().getFirstName());
        assertEquals("Mustermann", result.getBody().getLastName());
    }

    @Test
    void testCheckIfUserExists_returns_ok_when_user_exists() {
        var existsView = new UserExistsView("Max", "Mustermann", 1L);
        when(userHandler.checkIfUserExists(1L)).thenReturn(Optional.of(existsView));

        var result = userController.checkIfUserExists(1L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(existsView, result.getBody());
        verify(userHandler).checkIfUserExists(1L);
    }

    @Test
    void testCheckIfUserExists_returns_not_found_when_user_does_not_exist() {
        when(userHandler.checkIfUserExists(1L)).thenReturn(Optional.empty());

        var result = userController.checkIfUserExists(1L);

        assertEquals(404, result.getStatusCode().value());
        assertNull(result.getBody());
    }

    @Test
    void testFindAllUsers_returns_all_users_from_service() {
        when(userHandler.findAllUsers()).thenReturn(List.of(userView));

        var result = userController.findAllUsers();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(List.of(userView), result.getBody());
        verify(userHandler).findAllUsers();
    }

    @Test
    void testFindUserById_returns_user_when_user_exists() {
        when(userHandler.findUserById(1L)).thenReturn(Optional.of(userView));

        var result = userController.findUserById(1L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(userView, result.getBody());
        verify(userHandler).findUserById(1L);
    }

    @Test
    void testFindUserById_returns_not_found_when_user_does_not_exist() {
        when(userHandler.findUserById(1L)).thenReturn(Optional.empty());

        var result = userController.findUserById(1L);

        assertEquals(404, result.getStatusCode().value());
        assertNull(result.getBody());
    }

    @Test
    void testUpdateUserName_returns_updated_user_when_user_exists() {
        when(userHandler.updateUserName(1L, userDTO)).thenReturn(Optional.of(userView));

        var result = userController.updateUserName(1L, userDTO);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(userView, result.getBody());
        verify(userHandler).updateUserName(1L, userDTO);
    }

    @Test
    void testUpdateUserName_returns_not_found_when_user_does_not_exist() {
        when(userHandler.updateUserName(1L, userDTO)).thenReturn(Optional.empty());

        var result = userController.updateUserName(1L, userDTO);

        assertEquals(404, result.getStatusCode().value());
        assertNull(result.getBody());
    }

    @Test
    void testDeleteUser_returns_deleted_user_when_user_exists() {
        IUserDeleteView deleteView = mock(IUserDeleteView.class);
        when(userHandler.deleteUser(1L)).thenReturn(Optional.of(deleteView));

        var result = userController.deleteUser(1L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(deleteView, result.getBody());
        verify(userHandler).deleteUser(1L);
    }

    @Test
    void testDeleteUser_returns_not_found_when_user_does_not_exist() {
        when(userHandler.deleteUser(1L)).thenReturn(Optional.empty());

        var result = userController.deleteUser(1L);

        assertEquals(404, result.getStatusCode().value());
        assertNull(result.getBody());
    }

    @Test
    void testDepositToUser_correct_conversion_amount_and_decimals() {
        BigDecimal amount = new BigDecimal("100.00");
        int validDecimals = 20;
        long userId = 1L;

        BigDecimal expectedAmount = amount.add(BigDecimal.valueOf(validDecimals).movePointLeft(2));

        var response = userController.depositToUser(userId, amount, validDecimals);

        verify(userHandler).depositToUser(userId, expectedAmount);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Deposit successful", response.getBody());

    }

    @Test
    void testDepositToUser_returns_404_when_user_does_not_exist() {
        when(userHandler.depositToUser(anyLong(), any())).thenReturn(Optional.empty());

        var response = userController.depositToUser(1L, new BigDecimal("100.00"), 20);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testDepositToUser_invalid_amount() {
        BigDecimal invalidAmount = new BigDecimal("-100.00");
        int validDecimals = 20;
        long userId = 1L;

        verify(userHandler, never()).depositToUser(anyLong(), any());
        assertThrows(DecimalPlaceInvalidException.class,
                () -> userController.depositToUser(userId, invalidAmount, validDecimals));

    }

    @Test
    void testDepositToUser_invalid_decimals() {
        BigDecimal amount = new BigDecimal("100.00");
        int invalidDecimals = 20000;
        long userId = 1L;


        verify(userHandler, never()).depositToUser(anyLong(), any());
        assertThrows(DecimalPlaceInvalidException.class,
                () -> userController.depositToUser(userId, amount, invalidDecimals));
    }

    @Test
    void testDepositToUser_rejects_negative_decimals() {
        BigDecimal amount = new BigDecimal("100.00");

        assertThrows(DecimalPlaceInvalidException.class,
                () -> userController.depositToUser(1L, amount, -1));
        verify(userHandler, never()).depositToUser(anyLong(), any());
    }

    @Test
    void testDepositToUser_accepts_zero_decimals() {
        BigDecimal amount = new BigDecimal("100.00");

        var result = userController.depositToUser(1L, amount, 0);

        assertEquals(200, result.getStatusCode().value());
        verify(userHandler).depositToUser(1L, amount);
    }

    @Test
    void testWithdrawToUser_correct_conversion_amount_and_decimals() {
        BigDecimal amount = new BigDecimal("100.00");
        int validDecimals = 20;
        long userId = 1L;

        BigDecimal expectedAmount = amount.add(BigDecimal.valueOf(validDecimals).movePointLeft(2));

        var response = userController.withdrawFromUser(userId, amount, validDecimals);

        verify(userHandler).withdrawFromUser(userId, expectedAmount);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Withdraw successful", response.getBody());

    }

    @Test
    void testWithdrawToUser_returns_404_when_user_does_not_exist() {
        when(userHandler.withdrawFromUser(anyLong(), any())).thenReturn(Optional.empty());

        var response = userController.withdrawFromUser(1L, new BigDecimal("100.00"), 20);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testWithdrawToUser_invalid_decimals() {
        BigDecimal amount = new BigDecimal("100.00");
        int invalidDecimals = 20000;
        long userId = 1L;

        verify(userHandler, never()).withdrawFromUser(anyLong(), any());
        assertThrows(DecimalPlaceInvalidException.class,
                () -> userController.withdrawFromUser(userId, amount, invalidDecimals));
    }

    @Test
    void testWithdrawToUser_invalid_amount() {
        BigDecimal invalidAmount = new BigDecimal("-100.00");
        int validDecimals = 20;
        long userId = 1L;

        verify(userHandler, never()).withdrawFromUser(anyLong(), any());
        assertThrows(DecimalPlaceInvalidException.class,
                () -> userController.withdrawFromUser(userId, invalidAmount, validDecimals));

    }

    @Test
    void testWithdrawFromUser_rejects_negative_decimals() {
        BigDecimal amount = new BigDecimal("100.00");

        assertThrows(DecimalPlaceInvalidException.class,
                () -> userController.withdrawFromUser(1L, amount, -1));
        verify(userHandler, never()).withdrawFromUser(anyLong(), any());
    }

    @Test
    void testWithdrawFromUser_accepts_ninety_nine_decimals() {
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal expectedAmount = new BigDecimal("100.99");

        var result = userController.withdrawFromUser(1L, amount, 99);

        assertEquals(200, result.getStatusCode().value());
        verify(userHandler).withdrawFromUser(1L, expectedAmount);
    }

}
