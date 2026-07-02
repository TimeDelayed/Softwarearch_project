package com.instantwin.slotmachine.contract.model;

import java.math.BigDecimal;

import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;

public interface IThreeReelPayoutCalculator {
    BigDecimal calculateMultiplier(ThreeReelSpinDTO spinResult);
}
