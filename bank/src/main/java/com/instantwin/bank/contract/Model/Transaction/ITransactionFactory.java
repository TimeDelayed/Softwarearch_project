package com.instantwin.bank.contract.Model.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.Model.Transaction.TransactionEntity;
import com.instantwin.bank.Utilities.Transaction.TransactionInvoicingParty;

public interface ITransactionFactory {
    TransactionEntity createTransaction(long userId ,BigDecimal amount, TransactionInvoicingParty invoicingParty);
}
