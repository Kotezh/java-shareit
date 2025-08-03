package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCreateDto {
    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 1, max = 255, message = "Максимальная длина названия — 255 символов")
    private String name;

    @NotBlank
    @Size(min = 1, max = 1000, message = "Максимальная длина описания — 1000 символов")
    private String description;

    @NotNull
    private Boolean available;

    private Long requestId;

    private Long owner;
}
