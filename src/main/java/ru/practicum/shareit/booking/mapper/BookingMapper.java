package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@UtilityClass
public class BookingMapper {
    public static BookingDto mapToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .end(booking.getEnd())
                .start(booking.getStart())
                .status(booking.getStatus())
                .itemId(booking.getItemId())
                .bookerId(booking.getBookerId())
                .build();
    }

    public static Booking mapToBooking(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .end(bookingDto.getEnd())
                .start(bookingDto.getStart())
                .status(bookingDto.getStatus())
                .itemId(bookingDto.getItemId())
                .bookerId(bookingDto.getBookerId())
                .build();
    }

    public static List<BookingDto> mapToDtoList(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::mapToDto).toList();
    }

    public static List<Booking> mapToBookingList(List<BookingDto> bookingsDto) {
        return bookingsDto.stream().map(BookingMapper::mapToBooking).toList();
    }
}
