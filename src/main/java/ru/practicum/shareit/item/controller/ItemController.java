package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;
    private final String customHeader = "X-Sharer-User-Id";

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable @Positive Long itemId) {
        log.info("Получение вещи по id {}", itemId);
        return itemService.get(itemId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(customHeader) @Positive Long userId,
                          @Valid @RequestBody ItemDto itemDto) {
        itemDto.setOwnerId(userId);
        validateItemDto(itemDto);
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(customHeader) Long userId, @PathVariable @Positive Long itemId,
                          @Valid @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        itemDto.setOwnerId(userId);
        return itemService.update(itemDto, userId);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwnerId(@RequestHeader(customHeader) @Positive Long userId) {
        log.info("Запрос всех вещей пользователя {}", userId);
        return itemService.getItemsByOwnerId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByNameOrDescription(@RequestParam String text) {
        log.info("Поиск вещей по наименованию или описанию {}", text);
        return itemService.getItemsByNameOrDescription(text);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto create(
            @PathVariable Long itemId,
            @RequestHeader(value = customHeader, required = true) @Positive Long userId,
            @Valid @RequestBody CommentDto comment) {
        log.info("Создание отзыва пользователя {} на вещь {}", userId, itemId);
        comment.setAuthorId(userId);
        comment.setItemId(itemId);
        return commentService.create(comment);
    }

    public static void validateItemDto(@Valid ItemDto item) {
        if (item.getOwnerId() == null) {
            throw new ValidationException("Укажите владельца");
        }

        if (item.getName() != null && item.getName().isBlank()) {
            throw new ValidationException("Укажите название");
        }

        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Задайте описание");
        }

        if (item.getAvailable() == null) {
            throw new ValidationException("Укажите статус о том, доступна или нет вещь для аренды");
        }
    }
}
