package com.instantwin.slotmachine.utilities;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Symbol that can appear on a slot reel.")
public enum SlotSymbols {
    CHERRY,
    LEMON,
    BELL,
    DIAMOND;
}
