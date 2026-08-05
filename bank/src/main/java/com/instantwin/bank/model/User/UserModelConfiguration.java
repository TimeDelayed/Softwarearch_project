package com.instantwin.bank.model.User;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.instantwin.bank.contract.Model.User.IUserFactory;

@Configuration
public class UserModelConfiguration {
    
    @Bean
    IUserFactory userFactory() {
        return new UserFactory();
    }
}
