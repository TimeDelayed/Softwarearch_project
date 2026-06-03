package com.instantwin.bank.Model.User;

import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.instantwin.bank.Utilities.ModelValidityBreachException;
import com.instantwin.bank.contract.Model.User.IUserFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
public class UserFactoryTest {
   @Test
    void testCreateUser_should_call_UserEntity_of() throws ModelValidityBreachException {

        IUserFactory factory = new UserFactory();

        try (MockedStatic<UserEntity> mocked =
                     mockStatic(UserEntity.class)) {

            UserEntity user = mock(UserEntity.class);

            mocked.when(() -> UserEntity.of("Max", "Mustermann"))
                  .thenReturn(user);

            factory.createUser("Max", "Mustermann");

            mocked.verify(() -> UserEntity.of("Max", "Mustermann"));
        }
    }
}
