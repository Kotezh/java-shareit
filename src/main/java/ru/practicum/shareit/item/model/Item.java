package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @NotNull
    @Column(name = "is_available", nullable = false)
    private Boolean available;

    @NotNull
    @Positive
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Positive
    @Column(name = "request_id")
    private Long requestId;
}