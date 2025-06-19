package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
public class Booking {
    private Long id;
    private Long itemId;
    private Long bookerId;
    private Status status;
    private LocalDateTime start;
    private LocalDateTime end;
}
