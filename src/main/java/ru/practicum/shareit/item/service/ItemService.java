package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto item, Long userId);

    ItemDto update(ItemDto item, Long userId);

    void delete(Long itemId);

    ItemDto get(long itemId);

    List<ItemDto> getByIds(List<Long> ids);

    List<ItemDto> getItemsByOwnerId(long ownerId);

    List<ItemDto> getItemsByNameOrDescription(String text);

    boolean checkIdExist(Long id);

    boolean isAvailable(Long itemId);
}