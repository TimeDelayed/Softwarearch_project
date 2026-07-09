package com.instantwin.slotmachine.utilities;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

public class SlotResponseMapper {
    public static <T> ResponseEntity<T> optionalToResponseEntity(Optional<T> optional) {
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optional.get());
    }

}
