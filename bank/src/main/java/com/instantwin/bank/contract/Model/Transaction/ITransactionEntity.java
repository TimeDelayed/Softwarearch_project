package com.instantwin.bank.contract.Model.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.utilities.Transaction.TransactionInvoicingParty;

public interface ITransactionEntity {
    
    Long getId();
    Long getUserId();
    BigDecimal getAmount();
    TransactionInvoicingParty getInvoicingParty();
    

}
