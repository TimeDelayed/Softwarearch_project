package com.instantwin.bank.Model.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.Utilities.Transaction.TransactionInvoicingParty;
import com.instantwin.bank.contract.Model.Transaction.ITransactionFactory;

public class TransactionFactory implements ITransactionFactory {

    @Override
    public TransactionEntity createTransaction(long userId, BigDecimal amount,
            TransactionInvoicingParty invoicingParty) {
        return TransactionEntity.of(userId, invoicingParty, amount);
    }

}
