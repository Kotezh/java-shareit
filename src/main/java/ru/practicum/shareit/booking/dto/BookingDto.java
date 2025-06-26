package ru.practicum.shareit.booking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder(toBuilder = true)
@StartBeforeEnd
public class BookingDto {
    @Positive
    private Long id;

    @NotNull
    @Positive
    private Long itemId;

    @Positive
    private Long bookerId;

    private Status status;

    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;

    @Valid
    private ItemDto item;

    @Valid
    private UserDto booker;
//
//    @AssertTrue
//    boolean isStartBeforeEnd() {
//        return start.isBefore(end);
//    }
}