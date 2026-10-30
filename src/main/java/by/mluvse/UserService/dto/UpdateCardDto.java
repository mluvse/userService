package by.mluvse.UserService.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UpdateCardDto(@NotBlank @Pattern(regexp = "\\d{13,19}") String number,
                            @NotBlank String holder,
                            @NotNull @Future LocalDate expirationDate) {
}
