package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;


@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @Validated @RequestBody ItemRequestCreateDto createDto) {
        log.info("Создание запроса {} пользователя с id {}", createDto, userId);
        return itemRequestClient.createItemRequest(userId, createDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос данных о запросах пользователя по id {}", userId);
        return itemRequestClient.getItemRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsByNotUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос данных о всех запросах вещей по id {}", userId);
        return itemRequestClient.getItemRequestsByNotUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable Long requestId) {
        log.info("Запрос данных о запросе {} пользователя по id {}", requestId, userId);
        return itemRequestClient.getItemRequestById(userId, requestId);
    }
}
