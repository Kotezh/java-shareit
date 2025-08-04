package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    CommentDtoReturn createComment(long authorId, long itemId, CommentCreateDto createDto);

    ItemCommentDto getByItemId(long userId, long itemId);

    List<ItemCommentDto> getAllItems(Long ownerId);

    ItemDto createItem(ItemCreateDto createDto, long ownerId);

    ItemDto updateItem(ItemUpdateDto updateDto, long ownerId, long itemId);

    List<ItemDto> getItemsByNameOrDescription(String text);
}
