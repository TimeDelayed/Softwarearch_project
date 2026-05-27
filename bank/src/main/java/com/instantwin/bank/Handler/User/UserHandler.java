package com.instantwin.bank.Handler.User;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.instantwin.bank.Model.User.UserEntity;
import com.instantwin.bank.Repository.User.IUserRepository;
import com.instantwin.bank.Utilities.ModelValidityBreachException;
import com.instantwin.bank.View.User.UserDeleteView;
import com.instantwin.bank.View.User.UserView;
import com.instantwin.bank.contract.DTO.IUserDTO;
import com.instantwin.bank.contract.Handler.User.IUserHandler;
import com.instantwin.bank.contract.Model.User.IUserFactory;
import com.instantwin.bank.contract.View.User.IUserDeleteView;
import com.instantwin.bank.contract.View.User.IUserView;

@Service
public class UserHandler implements IUserHandler {

    private final IUserRepository userRepository;
    private final IUserFactory userFactory;

    public UserHandler(IUserRepository userRepository, IUserFactory userFactory) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
    }

    @Override
    public List<IUserView> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserView::of)
                .toList();
    }

    @Override
    public Optional<IUserView> findUserById(long id) {
        var result = userRepository.findById(id);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(UserView.of(result.get()));
    }

    @Override
    public IUserView createUser(IUserDTO userDTO) throws ModelValidityBreachException {

        UserEntity userEntity = userFactory.createUser(userDTO.getFirstName(), userDTO.getLastName());
        userRepository.save(userEntity);

        return UserView.of(userEntity);
    }

    @Override
    public Optional<IUserView> updateUserName(long id, IUserDTO userDTO) throws ModelValidityBreachException {
        var result = userRepository.findById(id);
        if (result.isEmpty()) {
            return Optional.empty();
        }

        UserEntity userEntity = result.get();
        userEntity.changeFirstName(userDTO.getFirstName());
        userEntity.changeLastName(userDTO.getLastName());

        userRepository.save(userEntity);

        return Optional.of(UserView.of(userEntity));
    }

    @Override
    public Optional<IUserDeleteView> deleteUser(long id) {
        var result = userRepository.findById(id);
        if (result.isEmpty()) {
            return Optional.empty();
        }

        UserEntity userEntity = result.get();
        userRepository.delete(userEntity);

        return Optional.of(UserDeleteView.of(userEntity));
    }
}
