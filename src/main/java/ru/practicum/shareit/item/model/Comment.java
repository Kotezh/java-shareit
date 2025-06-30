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
    @Column(name = "id")
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "item_id", nullable = false)
    @Positive
    private Long itemId;

    @Column(name = "author_id", nullable = false)
    @Positive
    private Long authorId;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}