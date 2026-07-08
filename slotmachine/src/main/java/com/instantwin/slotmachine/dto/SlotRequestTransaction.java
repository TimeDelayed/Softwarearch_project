package com.instantwin.slotmachine.dto;

import java.math.BigDecimal;

public record SlotRequestTransaction(String invoicingParty, BigDecimal amount) {
    
    String getInvoicingParty() {
        return invoicingParty;
    }

    BigDecimal getAmount() {
        return amount;
    }
}
