package com.instantwin.slotmachine.contract.model;

public interface ISlotConfiguration extends ISlotProbabilityConfiguration, ISlotPayoutConfiguration {
    int getNumberOfReels();
}
