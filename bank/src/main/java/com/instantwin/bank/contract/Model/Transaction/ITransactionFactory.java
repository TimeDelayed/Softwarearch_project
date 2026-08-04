package com.instantwin.bank.contract.Model.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.model.Transaction.TransactionEntity;
import com.instantwin.bank.utilities.Transaction.TransactionInvoicingParty;

public interface ITransactionFactory {
    TransactionEntity createTransaction(long userId ,BigDecimal amount, TransactionInvoicingParty invoicingParty);
}
