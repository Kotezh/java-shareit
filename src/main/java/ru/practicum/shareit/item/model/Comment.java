package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(name = "item_id", nullable = false)
    @Positive
    private Long itemId;

    @Column(name = "author_id", nullable = false)
    @Positive
    private Long authorId;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}