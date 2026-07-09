package com.instantwin.slotmachine.dto;

import java.math.BigDecimal;

public record SlotRequestTransactionDTO(String invoicingParty, BigDecimal amount) {
    
    String getInvoicingParty() {
        return invoicingParty;
    }

    BigDecimal getAmount() {
        return amount;
    }
}
