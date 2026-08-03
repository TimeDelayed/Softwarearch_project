package com.instantwin.slotmachine.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;
import com.instantwin.slotmachine.utilities.ModelValidityBreachException;
import com.instantwin.slotmachine.utilities.SlotErrorMessages;
import com.instantwin.slotmachine.utilities.SlotSymbols;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "slot_game")
@Getter
public class SlotGameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "won", nullable = false)
    private boolean won;

    @Column(name = "betAmount", nullable = false)
    private BigDecimal betAmount;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "slotStates", nullable = false)
    private List<SlotSymbols> slotStates = new ArrayList<>();

    protected SlotGameEntity() {
    }

    private SlotGameEntity(Long userId, BigDecimal betAmount, boolean won, BigDecimal amount,
            List<SlotSymbols> slotStates) {
        this.userId = userId;
        this.won = won;
        this.amount = amount;
        this.slotStates = slotStates;
        this.betAmount = betAmount;
    }

    private static void validateSlotGameEntity(Long userId, BigDecimal betAmount, boolean won, BigDecimal netAmount,
            ThreeReelSpinDTO spinResult) {
        if (userId == null) {
            throw new ModelValidityBreachException(SlotErrorMessages.USER_ID_NULL);
        }
        if (userId < 0) {
            throw new ModelValidityBreachException(SlotErrorMessages.USER_ID_NEGATIVE);
        }
        if (netAmount == null || betAmount == null) {
            throw new ModelValidityBreachException(SlotErrorMessages.INVALID_AMOUNT_NULL);
        }
        if (betAmount.signum() < 0) {
            throw new ModelValidityBreachException(SlotErrorMessages.INVALID_AMOUNT_NEGATIVE);
        }

        validateThreeReelSpinDTO(spinResult);
    }

    private static void validateThreeReelSpinDTO(ThreeReelSpinDTO spinResult) {
        if (spinResult == null) {
            throw new ModelValidityBreachException(SlotErrorMessages.INVALID_SLOT_STATES_NULL);
        }
        if (spinResult.first() == null || spinResult.second() == null || spinResult.third() == null) {
            throw new ModelValidityBreachException(SlotErrorMessages.INVALID_SLOT_STATES_NULL);
        }
    }

    private static List<SlotSymbols> convertSpinResultToSlotStates(ThreeReelSpinDTO spinResult) {
        List<SlotSymbols> slotStates = new ArrayList<>();
        slotStates.add(spinResult.first());
        slotStates.add(spinResult.second());
        slotStates.add(spinResult.third());
        return slotStates;
    }

    public static SlotGameEntity of(Long userId, BigDecimal betAmount, boolean won, BigDecimal netAmount,
            ThreeReelSpinDTO spinResult) {
        validateSlotGameEntity(userId, betAmount, won, netAmount, spinResult);
        List<SlotSymbols> slotStates = convertSpinResultToSlotStates(spinResult);
        return new SlotGameEntity(userId, betAmount, won, netAmount, slotStates);
    }

}
