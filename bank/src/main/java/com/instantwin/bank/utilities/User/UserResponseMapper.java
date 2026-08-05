package com.instantwin.bank.utilities.User;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

public class UserResponseMapper {
    public static <T> ResponseEntity<T> optionalToResponseEntity(Optional<T> optional) {
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optional.get());
    }
}