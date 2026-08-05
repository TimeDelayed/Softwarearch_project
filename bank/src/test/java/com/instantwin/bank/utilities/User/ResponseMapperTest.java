package com.instantwin.bank.utilities.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.instantwin.bank.utilities.User.UserResponseMapper;

public class ResponseMapperTest {
    @Test
    void testOptionalToResponseEntity_returns_ok_when_Optional_has_value() {
        Optional<String> optional = Optional.of("test");

        ResponseEntity<String> result =
                UserResponseMapper.optionalToResponseEntity(optional);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("test", result.getBody());
    }

    @Test
    void testOptionalToResponseEntity_returns_NotFound_when_Optional_is_Empty() {
        Optional<String> optional = Optional.empty();

        ResponseEntity<String> result =
                UserResponseMapper.optionalToResponseEntity(optional);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

}
