package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;


public interface CommentService {

    List<CommentDto> getAll();

    CommentDto get(Long id);

    CommentDto create(CommentDto comment);

    CommentDto update(CommentDto comment);

    void delete(Long id);

    List<CommentDto> findByItemId(Long itemId);

    List<CommentDto> findByItemIds(List<Long> itemIds);

    List<CommentDto> findByAuthorId(Long authorId);
}