package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto bookItem(BookingCreateDto createDto, long bookerId);

    BookingDto updateStatus(long userId, long bookingId, boolean approve);

    BookingDto getBookingById(long userId, long bookingId);

    List<BookingDto> getBookingsByBooker(long booker, String state);

    List<BookingDto> getBookingsByOwner(long ownerId, String state);
}
