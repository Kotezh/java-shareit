package ru.practicum.shareit.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByItemIdIn(List<Long> itemIds);

    List<Comment> findByItemId(Long itemId);

    List<Comment> findByAuthorId(Long authorId);
}