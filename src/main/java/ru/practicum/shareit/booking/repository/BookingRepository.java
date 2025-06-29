package ru.practicum.shareit.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.Status;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> getByBookerId(Long userId);

    List<Booking> getByBookerIdAndStatus(Long userId, Status status);

    List<Booking> getByBookerIdAndItemIdAndStatus(Long userId, Long itemId, Status status);

    List<Booking> getByItemIdInAndStatus(List<Long> itemsIds, Status status);

    @Query(value = "SELECT EXISTS (" +
            "SELECT 1 " +
            "FROM bookings " +
            "WHERE item_id = :itemId " +
            "AND booker_id = :bookerId " +
            "AND status = :statusId " +
            "LIMIT 1" +
            ")", nativeQuery = true)
    Boolean checkItemOfBookerWithStatus(
            @Param("bookerId") Long bookerId,
            @Param("itemId") Long itemId,
            @Param("statusId") Integer statusId);
}