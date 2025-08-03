package ru.practicum.shareit.booking.controller;

import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

@Controller
@RequestMapping(path = "/bookings")
@Validated
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingClient bookingClient;
    private final String customHeader = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getBookingsByBooker(
            @RequestHeader(value = customHeader, required = true) @Positive Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size
            ) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Запрос всех бронирований пользователя state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookingsByBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(
            @RequestHeader(value = customHeader, required = false) @Positive Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size
            ) {
        BookingState state = BookingState.from(bookingState)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + bookingState));
        log.info("Запрос владельца вещей бронирований userId={}, from={}, size={}", userId, from, size);
        return bookingClient.getBookingsByOwner(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(
            @RequestHeader(value = customHeader, required = true) @Positive Long userId,
            @Valid @RequestBody BookItemRequestDto requestDto
            ) {
        log.info("Создание BookingDto {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(
            @RequestHeader(value = customHeader, required = true) @Positive Long userId,
            @PathVariable Long bookingId,
            @RequestParam(defaultValue = "true") Boolean approved) {
        log.info("Обновление бронирования {} пользователем {}", bookingId, userId);
        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(value = customHeader, required = true) @Positive Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Запрос бронирования {} по id и по userId {}", bookingId, userId);
        return bookingClient.getBookingById(bookingId, userId);
    }
}
