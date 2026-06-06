package com.instantwin.bank.Service.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.instantwin.bank.DTO.User.UserRequestTransaction;
import com.instantwin.bank.Model.User.UserEntity;
import com.instantwin.bank.Repository.User.IUserRepository;
import com.instantwin.bank.Utilities.UserBalanceCalculator;
import com.instantwin.bank.View.User.UserDeleteView;
import com.instantwin.bank.View.User.UserView;
import com.instantwin.bank.contract.Client.IUserTransactionClient;
import com.instantwin.bank.contract.DTO.IUserDTO;
import com.instantwin.bank.contract.Model.User.IUserFactory;
import com.instantwin.bank.contract.Service.User.IUserService;
import com.instantwin.bank.contract.View.User.IUserDeleteView;
import com.instantwin.bank.contract.View.User.IUserView;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IUserFactory userFactory;
    private final IUserTransactionClient transactionClient;

    public UserService(
        IUserRepository userRepository,
        IUserFactory userFactory,
        IUserTransactionClient transactionClient
        ) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.transactionClient = transactionClient;
    }

    private BigDecimal getUserBalance(long userId) {
        var transactions = transactionClient.getAllTransactionsForUser(userId);
        return UserBalanceCalculator.calculateBalanceForUser(transactions);
    }

    @Override
    public List<IUserView> findAllUsers() {
        return userRepository.findAll().stream()
                .map((userEntity) -> UserView.of(userEntity, getUserBalance(userEntity.getId())))
                .toList();
    }

    @Override
    public Optional<IUserView> findUserById(long id) {
        var result = userRepository.findById(id);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(UserView.of(result.get(), getUserBalance(id)));
    }

    @Override
    public IUserView createUser(IUserDTO userDTO) {

        UserEntity userEntity = userFactory.createUser(userDTO.getFirstName(), userDTO.getLastName());
        userRepository.save(userEntity);

        return UserView.of(userEntity, getUserBalance(userEntity.getId()));
    }

    @Override
    public Optional<IUserView> updateUserName(long id, IUserDTO userDTO) {
        var result = userRepository.findById(id);
        if (result.isEmpty()) {
            return Optional.empty();
        }

        UserEntity userEntity = result.get();
        userEntity.changeFirstName(userDTO.getFirstName());
        userEntity.changeLastName(userDTO.getLastName());

        userRepository.save(userEntity);

        return Optional.of(UserView.of(userEntity, getUserBalance(id)));
    }

    @Override
    public Optional<IUserDeleteView> deleteUser(long id) {
        var result = userRepository.findById(id);
        if (result.isEmpty()) {
            return Optional.empty();
        }

        BigDecimal balance = getUserBalance(id);

        UserEntity userEntity = result.get();
        userRepository.delete(userEntity);

        return Optional.of(UserDeleteView.of(userEntity, balance));
    }

    @Override
    public ResponseEntity<String> depositToUser(long id, BigDecimal amount) {
        var transactionServiceResponse = transactionClient.depositTransaction(id, amount);
        return transactionServiceResponse;
    }

    @Override
    public ResponseEntity<String> withdrawFromUser(long id, BigDecimal amount) {
        var transactionServiceResponse = transactionClient.withdrawTransaction(id, amount);
        return transactionServiceResponse;
    }


}
