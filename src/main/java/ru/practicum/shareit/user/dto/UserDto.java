package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @Positive(message = "Id должен быть положительным")
    private Long id;

    //    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 3, max = 100)
    private String name;

    //    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта должна содержать символ @")
    @Size(min = 3, max = 255)
    private String email;
}
