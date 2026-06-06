package com.instantwin.bank.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.instantwin.bank.DTO.User.UserDTO;
import com.instantwin.bank.Utilities.DecimalPlaceInvalidException;
import com.instantwin.bank.Utilities.ErrorMessages;
import com.instantwin.bank.Utilities.ResponseMapper;
import com.instantwin.bank.contract.Controller.IUserController;
import com.instantwin.bank.contract.DTO.IUserDTO;
import com.instantwin.bank.contract.Service.User.IUserService;
import com.instantwin.bank.contract.View.User.IUserDeleteView;
import com.instantwin.bank.contract.View.User.IUserView;

@RestController
public class UserController implements IUserController {

    private final IUserService userHandler;

    public UserController(IUserService userHandler) {
        this.userHandler = userHandler;
    }

    @Override
    public ResponseEntity<List<IUserView>> findAllUsers() {
        return ResponseEntity.ok(userHandler.findAllUsers());
    }

    @Override
    public ResponseEntity<IUserView> findUserById(long id) {
        var result = userHandler.findUserById(id);

        return ResponseMapper.optionalToResponseEntity(result);
    }

    @Override
    public ResponseEntity<IUserView> createUser(UserDTO userDTO) {
        var result = userHandler.createUser(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<IUserView> updateUserName(long id, IUserDTO userDTO) {

        var result = userHandler.updateUserName(id, userDTO);

        return ResponseMapper.optionalToResponseEntity(result);
    }

    @Override
    public ResponseEntity<IUserDeleteView> deleteUser(long id) {
        var result = userHandler.deleteUser(id);
        
        return ResponseMapper.optionalToResponseEntity(result);
    }

    private void validateDecimalInput(int decimals) {
        boolean decimalInputInvalid = decimals < 0 || decimals > 99;
        if (decimalInputInvalid) {
            throw new DecimalPlaceInvalidException(ErrorMessages.DECIMAL_INPUT_INVALID);
        }
    }

    private BigDecimal convertToFullAmount(BigDecimal amount, int decimals) {
        return amount.add(
                BigDecimal.valueOf(decimals)
                        .movePointLeft(2));
    }

    @Override
    public ResponseEntity<String> depositToUser(long id, BigDecimal amount, int decimals) {

        validateDecimalInput(decimals);

        BigDecimal fullAmount = convertToFullAmount(amount, decimals);
        var transactionResponse = userHandler.depositToUser(id, fullAmount);
        return transactionResponse;
    }

    @Override
    public ResponseEntity<String> withdrawFromUser(long id, BigDecimal amount, int decimals) {

        validateDecimalInput(decimals);

        BigDecimal fullAmount = convertToFullAmount(amount, decimals);
        var transactionResponse = userHandler.withdrawFromUser(id, fullAmount);
        return transactionResponse;
    }

}
