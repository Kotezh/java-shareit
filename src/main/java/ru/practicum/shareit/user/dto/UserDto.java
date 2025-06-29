package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @Size(min = 2, max = 100)
    private String name;

    @Email(message = "Электронная почта должна содержать символ @")
    @Size(min = 2, max = 255)
    private String email;
}
