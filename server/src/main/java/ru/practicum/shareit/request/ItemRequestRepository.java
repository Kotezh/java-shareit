package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("""
            SELECT ir
            FROM ItemRequest ir
            WHERE ir.requester.id = :requesterId
            ORDER BY ir.created DESC
            """)
    List<ItemRequest> findAllByRequesterIdSorted(@Param("requesterId") Long requesterId);

    @Query("""
            SELECT ir
            FROM ItemRequest ir
            WHERE ir.requester.id <> :requesterId
            ORDER BY ir.created DESC
            """)
    List<ItemRequest> findAllByNotRequesterIdSorted(@Param("requesterId") Long requesterId);

}
