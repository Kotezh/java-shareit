package ru.practicum.shareit.item.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    @NotEmpty
    @Size(min = 1, max = 1000)
    private String text;
    @Positive
    private Long itemId;
    @Positive
    private Long authorId;
    private String authorName;
    private LocalDateTime createdAt;
}