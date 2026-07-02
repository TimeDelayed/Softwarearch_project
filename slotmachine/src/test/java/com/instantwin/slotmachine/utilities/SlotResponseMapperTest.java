package com.instantwin.slotmachine.utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class SlotResponseMapperTest {

    @Test
    void testOptionalToResponseEntity_returns_ok_when_optional_has_value() {
        var result = SlotResponseMapper.optionalToResponseEntity(Optional.of("test"));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("test", result.getBody());
    }

    @Test
    void testOptionalToResponseEntity_returns_not_found_when_optional_is_empty() {
        var result = SlotResponseMapper.optionalToResponseEntity(Optional.empty());

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }
}
