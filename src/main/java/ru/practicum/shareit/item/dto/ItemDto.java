package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @NotNull
    @Size(min = 1, max = 255, message = "Максимальная длина описания — 255 символов")
    private String description;
    private Boolean available;
    @Positive
    @NotNull
    private Long ownerId;
    @Positive
    private Long requestId;
    private List<CommentDto> comments;
    private Long lastBooking;
    private Long nextBooking;
}
