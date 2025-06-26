package ru.practicum.shareit.item.mapper;

import java.util.List;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@UtilityClass
public class CommentMapper {
    public static CommentDto mapToDto(Comment comment) {
        if (comment == null) return null;
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemId(comment.getItemId())
                .authorId(comment.getAuthorId())
                .createdAt(comment.getCreatedAt()).build();
    }

    public static Comment mapToComment(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .itemId(commentDto.getItemId())
                .authorId(commentDto.getAuthorId())
                .createdAt(commentDto.getCreatedAt()).build();
    }


    public static List<Comment> mapToCommentList(List<CommentDto> commentsDto) {
        return commentsDto.stream().map(CommentMapper::mapToComment).toList();
    }

    public static List<CommentDto> mapToDtoList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::mapToDto).toList();
    }
}