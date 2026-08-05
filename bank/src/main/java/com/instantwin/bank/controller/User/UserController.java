package com.instantwin.bank.controller.User;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.instantwin.bank.DTO.User.UserDTO;
import com.instantwin.bank.contract.Controller.User.IUserController;
import com.instantwin.bank.contract.Service.User.IUserService;
import com.instantwin.bank.contract.View.User.IUserDeleteView;
import com.instantwin.bank.contract.View.User.IUserView;
import com.instantwin.bank.utilities.User.DecimalPlaceInvalidException;
import com.instantwin.bank.utilities.User.UserErrorMessages;
import com.instantwin.bank.utilities.User.UserResponseMapper;
import com.instantwin.bank.view.User.UserExistsView;

@Validated
@RestController
public class UserController implements IUserController {

    private final IUserService userHandler;

    public UserController(IUserService userHandler) {
        this.userHandler = userHandler;
    }

    @Override
    public ResponseEntity<UserExistsView> checkIfUserExists(long id) {
        return UserResponseMapper.optionalToResponseEntity(userHandler.checkIfUserExists(id));
    }

    @Override
    public ResponseEntity<List<IUserView>> findAllUsers() {
        return ResponseEntity.ok(userHandler.findAllUsers());
    }

    @Override
    public ResponseEntity<IUserView> findUserById(long id) {
        var result = userHandler.findUserById(id);

        return UserResponseMapper.optionalToResponseEntity(result);
    }

    @Override
    public ResponseEntity<IUserView> createUser(UserDTO userDTO) {
        var result = userHandler.createUser(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<IUserView> updateUserName(long id, UserDTO userDTO) {

        var result = userHandler.updateUserName(id, userDTO);

        return UserResponseMapper.optionalToResponseEntity(result);
    }

    @Override
    public ResponseEntity<IUserDeleteView> deleteUser(long id) {
        var result = userHandler.deleteUser(id);
        
        return UserResponseMapper.optionalToResponseEntity(result);
    }

    private void validateDecimalInput(int decimals) {
        boolean decimalInputInvalid = decimals < 0 || decimals > 99;
        if (decimalInputInvalid) {
            throw new DecimalPlaceInvalidException(UserErrorMessages.DECIMAL_INPUT_INVALID);
        }
    }

    private void validateAmountInput(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DecimalPlaceInvalidException(UserErrorMessages.AMOUNT_INPUT_INVALID);
        }
    }

    private BigDecimal convertToFullAmount(BigDecimal amount, int decimals) {
        return amount.add(
                BigDecimal.valueOf(decimals)
                        .movePointLeft(2));
    }

    @Override
    public ResponseEntity<String> depositToUser(long id, BigDecimal amount, int decimals) {
        validateAmountInput(amount);
        validateDecimalInput(decimals);

        BigDecimal fullAmount = convertToFullAmount(amount, decimals);
        var transactionResponse = userHandler.depositToUser(id, fullAmount);
        return UserResponseMapper.optionalToResponseEntity(transactionResponse);
    }

    @Override
    public ResponseEntity<String> withdrawFromUser(long id, BigDecimal amount, int decimals) {
        validateAmountInput(amount);
        validateDecimalInput(decimals);

        BigDecimal fullAmount = convertToFullAmount(amount, decimals);
        var transactionResponse = userHandler.withdrawFromUser(id, fullAmount);
        return UserResponseMapper.optionalToResponseEntity(transactionResponse);
    }

}
