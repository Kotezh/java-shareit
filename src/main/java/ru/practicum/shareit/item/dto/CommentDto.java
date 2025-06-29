package ru.practicum.shareit.item.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;

    private String text;

    @Positive
    private Long itemId;

    @Positive
    private Long authorId;

    private String authorName;

    private LocalDateTime created;
}