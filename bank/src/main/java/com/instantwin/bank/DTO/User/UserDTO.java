package com.instantwin.bank.DTO.User;

import com.instantwin.bank.contract.DTO.User.IUserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data Transfer Object representing a user in the banking system.")
public record UserDTO(
        @NotNull @NotBlank @Schema(description = "Name of user", example = "Max") String firstName,
        @NotNull @NotBlank @Schema(description = "Surname of user", example = "Mustermann") String lastName)
        implements IUserDTO {

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

}
