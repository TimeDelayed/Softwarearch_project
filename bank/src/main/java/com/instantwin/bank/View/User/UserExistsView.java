package com.instantwin.bank.View.User;
import java.math.BigDecimal;
import com.instantwin.bank.contract.Model.User.IUserEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;
public record UserExistsView(
                @NotNull @NotBlank String firstName,
                @NotNull @NotBlank String lastName,
                @Positive long id) {

        public static UserExistsView of(IUserEntity user) {
                return new UserExistsView(user.getFirstName(), user.getLastName(), user.getId());
        }

        public String getFirstName() {
                return firstName;
        }

        public String getLastName() {
                return lastName;
        }

        public long getId() {
                return id;
        }

}
