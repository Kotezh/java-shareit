package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;

    @NotNull
    @Size(min = 2, max = 1000)
    private String description;

    @NotNull
    @Positive
    private Long requesterId;

    private LocalDateTime createdAt;
}