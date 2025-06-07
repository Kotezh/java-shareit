package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateDto {
    private Long id;
    @NotNull
    private String name;
    @Email
    @NotNull
    @NotBlank
    private String email;
}
