package com.instantwin.bank.Model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.instantwin.bank.Model.User.UserFactory;
import com.instantwin.bank.contract.Model.User.IUserFactory;

@Configuration
public class ModelConfiguration {
    
    @Bean
    IUserFactory userFactory() {
        return new UserFactory();
    }
}
// TODO: Do I need this?