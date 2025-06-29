package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Validated
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;
    private final String customHeader = "X-Sharer-User-Id";

    @GetMapping("/{bookingId}")
    public BookingDto get(@PathVariable Long bookingId) {
        log.info("Запрос бронирования {} по id", bookingId);
        return bookingService.get(bookingId);
    }

    @PostMapping
    public BookingDto create(
            @Valid @RequestBody BookingDto bookingDto,
            @RequestHeader(value = customHeader, required = true) @Positive Long userId) {
        log.info("Создание BookingDto {}", bookingDto);
        bookingDto.setBookerId(userId);
        validateBookingDto(bookingDto);
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(
            @PathVariable Long bookingId,
            @RequestHeader(value = customHeader, required = true) @Positive Long userId,
            @RequestParam(defaultValue = "true") boolean approved) {
        log.info("Обновление бронирования {} пользователем {}", bookingId, approved);
        return bookingService.updateStatus(bookingId, userId, approved);
    }

    @GetMapping
    public List<BookingDto> getByUser(
            @RequestParam(defaultValue = "ALL") Status status,
            @RequestHeader(value = customHeader, required = true) @Positive Long userId) {
        log.info("Запрос всех бронирований пользователя {}", userId);
        return bookingService.getByBookerId(userId, status);
    }

    @GetMapping("/owner")
    public List<BookingDto> getByOwner(
            @RequestParam(defaultValue = "ALL") Status status,
            @RequestHeader(value = customHeader, required = false) @Positive Long userId) {
        log.info("Запрос владельца вещей бронирований ИД {}", userId);
        return bookingService.getByOwnerId(userId, status);
    }

    public static void validateBookingDto(@Valid BookingDto bookingDto) {
        if (bookingDto.getStart() == null) {
            throw new ValidationException("Укажите дату начала бронирования");
        }
        if (bookingDto.getEnd() == null) {
            throw new ValidationException("Укажите дату окончания бронирования");
        }

        if (!isValidStartAndEndDates(bookingDto.getStart(), bookingDto.getEnd())) {
            throw new ValidationException("Укажите id вещи для комментирования");
        }
    }

    private static boolean isValidStartAndEndDates(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (!startDateTime.isBefore(endDateTime)) {
            return false;
        }
        if (startDateTime.isBefore(LocalDateTime.now()) || endDateTime.isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }
}
