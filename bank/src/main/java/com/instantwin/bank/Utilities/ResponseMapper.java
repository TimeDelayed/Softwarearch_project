package com.instantwin.bank.Utilities;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

public class ResponseMapper {
    public static <T> ResponseEntity<T> optionalToResponseEntity(Optional<T> optional) {
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optional.get());
    }
}
