package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto get(Long id);

    List<BookingDto> getByBookerId(Long userId, Status status);

    List<BookingDto> getByBookerIdAndItemIdAndStatus(Long userId, Long itemId, Status status);

    List<BookingDto> getByOwnerId(Long userId, Status status);

    BookingDto create(BookingDto booking, Long userId);

    BookingDto update(Long bookingId, BookingDto booking, Long userId);

    BookingDto updateStatus(Long bookingId, Long userId, boolean approved);

    void delete(Long id);

    boolean isIdExist(Long id);

    boolean isItemOfBookerWithStatus(Long bookerId, Long itemId, Status status);

//    List<BookingDto> getOwnerBookings(long userId, Status status);
}
