package com.instantwin.bank.model.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.contract.Model.Transaction.ITransactionFactory;
import com.instantwin.bank.utilities.Transaction.TransactionInvoicingParty;

public class TransactionFactory implements ITransactionFactory {

    @Override
    public TransactionEntity createTransaction(long userId, BigDecimal amount,
            TransactionInvoicingParty invoicingParty) {
        return TransactionEntity.of(userId, invoicingParty, amount);
    }

}
