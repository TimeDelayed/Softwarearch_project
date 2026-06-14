package com.instantwin.bank.Model.User;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.instantwin.bank.Model.Transaction.TransactionFactory;
import com.instantwin.bank.contract.Model.Transaction.ITransactionFactory;
import com.instantwin.bank.contract.Model.User.IUserFactory;

@Configuration
public class UserModelConfiguration {
    
    @Bean
    IUserFactory userFactory() {
        return new UserFactory();
    }
}
