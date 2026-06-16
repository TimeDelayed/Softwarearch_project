package com.instantwin.bank.Model.Transaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.instantwin.bank.contract.Model.Transaction.ITransactionFactory;

@Configuration
public class TransactionModelConfiguration {
    
    @Bean
    ITransactionFactory transactionFactory() {
        return new TransactionFactory();
    }
}
