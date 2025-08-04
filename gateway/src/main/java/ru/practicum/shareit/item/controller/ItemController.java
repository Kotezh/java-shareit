package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Validated
@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;
    private final String customHeader = "X-Sharer-User-Id";

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getByItemId(@PathVariable @Positive Long itemId, @RequestHeader(value = customHeader, required = true) Long userId) {
        log.info("Получение вещи с id {} пользователя {}", itemId, userId);
        return itemClient.getByItemId(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(value = customHeader, required = true) @Positive Long userId,
                                             @Valid @RequestBody ItemCreateDto itemDto) {
        log.info("Создание вещи {} пользователя {}", itemDto, userId);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(value = customHeader, required = true) Long userId,
                                             @PathVariable @Positive Long itemId,
                                             @RequestBody @Valid ItemUpdateDto itemDto) {
        log.info("Обновление вещи по id {} пользователя {}", itemId, userId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(value = customHeader, required = true) @Positive Long userId
                                              ) {
        log.info("Запрос всех вещей пользователя {}", userId);
        return itemClient.getAllItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByNameOrDescription(@RequestParam String text,
                                                              @RequestHeader(value = customHeader, required = true) @Positive Long userId) {
        log.info("Поиск вещей по наименованию или описанию {}", text);
        return itemClient.getItemsByNameOrDescription(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(
            @RequestHeader(value = customHeader, required = true) @Positive Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody CommentRequestDto commentDto) {
        log.info("Создание отзыва пользователя {} на вещь {}", userId, itemId);
        return itemClient.createComment(userId, itemId, commentDto);
    }
}
