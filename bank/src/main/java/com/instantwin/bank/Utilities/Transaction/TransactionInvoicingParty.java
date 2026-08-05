package com.instantwin.bank.utilities.Transaction;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enumeration representing the different parties that can be involved in a transaction for a user.")
public enum TransactionInvoicingParty {
    USER_SLICE,
    ROULETTE,
    SLOTS;
}
