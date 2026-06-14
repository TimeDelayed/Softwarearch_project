package com.instantwin.bank.Model.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.Utilities.Transaction.TransactionInvoicingParty;
import com.instantwin.bank.contract.Model.Transaction.ITransactionEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "transactions")
@Getter
public class TransactionEntity implements ITransactionEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "invoicingParty", nullable = false)
    private TransactionInvoicingParty invoicingParty;
    
    protected TransactionEntity() {
    }

    private TransactionEntity(TransactionInvoicingParty invoicingParty ,BigDecimal amount) {
        this.invoicingParty = invoicingParty;
        this.amount = amount;
    }

    public static TransactionEntity of(TransactionInvoicingParty invoicingParty, BigDecimal amount) {
        
        return new TransactionEntity(invoicingParty, amount);
    }
}
