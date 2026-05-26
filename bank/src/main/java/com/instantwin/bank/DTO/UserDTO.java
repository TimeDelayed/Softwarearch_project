package com.instantwin.bank.DTO;

import com.instantwin.bank.contract.DTO.IUserDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDTO(
        @NotNull @NotBlank String firstName,
        @NotNull @NotBlank String lastName) implements IUserDTO {

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

}
