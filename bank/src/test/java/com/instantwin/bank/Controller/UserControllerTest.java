package com.instantwin.bank.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.instantwin.bank.Controller.User.UserController;
import com.instantwin.bank.DTO.User.UserDTO;
import com.instantwin.bank.Model.User.UserEntity;
import com.instantwin.bank.Utilities.User.DecimalPlaceInvalidException;
import com.instantwin.bank.View.User.UserView;
import com.instantwin.bank.contract.Service.User.IUserService;
import com.instantwin.bank.contract.View.User.IUserView;

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
    void testDepositToUser_correct_conversion_amount_and_decimals() {
        BigDecimal amount = new BigDecimal("100.00");
        int validDecimals = 20;
        long userId = 1L;

        BigDecimal expectedAmount = amount.add(BigDecimal.valueOf(validDecimals).movePointLeft(2));

        userController.depositToUser(userId, amount, validDecimals);

        verify(userHandler).depositToUser(userId, expectedAmount);

    }

    @Test
    void testDepositToUser_invalid_decimals() {
        BigDecimal amount = new BigDecimal("100.00");
        int invalidDecimals = 20000;
        long userId = 1L;

        assertThrows(DecimalPlaceInvalidException.class,
                () -> userController.depositToUser(userId, amount, invalidDecimals));
    }

    @Test
    void testWithdrawToUser_correct_conversion_amount_and_decimals() {
        BigDecimal amount = new BigDecimal("100.00");
        int validDecimals = 20;
        long userId = 1L;

        BigDecimal expectedAmount = amount.add(BigDecimal.valueOf(validDecimals).movePointLeft(2));

        userController.withdrawFromUser(userId, amount, validDecimals);

        verify(userHandler).withdrawFromUser(userId, expectedAmount);

    }

    @Test
    void testWithdrawToUser_invalid_decimals() {
        BigDecimal amount = new BigDecimal("100.00");
        int invalidDecimals = 20000;
        long userId = 1L;
        assertThrows(DecimalPlaceInvalidException.class,
                () -> userController.withdrawFromUser(userId, amount, invalidDecimals));
    }

}
