package ru.practicum.shareit.item.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Autowired
    @Lazy
    private BookingService bookingService;

    @Override
    public List<CommentDto> getAll() {
        List<CommentDto> comments = commentRepository.findAll().stream()
                .map(CommentMapper::mapToDto)
                .toList();
        return addUserInfo(comments);
    }

    @Override
    public CommentDto get(Long id) {
        CommentDto comment = commentRepository.findById(id)
                .map(CommentMapper::mapToDto)
                .orElseThrow(() -> new NotFoundException("Комментарий с таким id не найден"));
        return addUserInfo(comment);
    }

    @Override
    @Transactional
    public CommentDto create(CommentDto commentDto) {
        checkItemBookedAndApproved(commentDto.getAuthorId(), commentDto.getItemId());
        return save(commentDto);
    }

    @Override
    @Transactional
    public CommentDto update(CommentDto commentDto) {
        return save(commentDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentDto> findByItemId(Long itemId) {
        List<CommentDto> commentsDto = CommentMapper.mapToDtoList(commentRepository.findByItemId(itemId));
        return addUserInfo(commentsDto);
    }

    @Override
    public List<CommentDto> findByItemIds(List<Long> itemIds) {
        List<CommentDto> commentsDto = CommentMapper.mapToDtoList(commentRepository.findByItemIdIn(itemIds));
        return addUserInfo(commentsDto);
    }

    @Override
    public List<CommentDto> findByAuthorId(Long authorId) {
        List<CommentDto> commentsDto = CommentMapper.mapToDtoList(commentRepository.findByAuthorId(authorId));
        return addUserInfo(commentsDto);
    }

    private List<CommentDto> addUserInfo(List<CommentDto> comments) {
        List<Long> authorsIds = comments.stream().map(CommentDto::getAuthorId).toList();
        List<UserDto> users = userService.getUsers(authorsIds);
        Map<Long, String> usersNames = users.stream()
                .collect(Collectors.toMap(
                        UserDto::getId,
                        UserDto::getName));

        return comments.stream()
                .peek(x -> {
                    String name = usersNames.getOrDefault(x.getAuthorId(), "no name");
                    x.setAuthorName(name);
                }).toList();
    }

    @Transactional
    private CommentDto addUserInfo(CommentDto comment) {
        return addUserInfo(List.of(comment)).getFirst();
    }

    @Transactional
    private CommentDto save(CommentDto commentDto) {
        Comment comment = CommentMapper.mapToComment(commentDto);
        CommentDto commentSaved = CommentMapper.mapToDto(commentRepository.save(comment));
        return addUserInfo(commentSaved);
    }

    private void checkItemBookedAndApproved(Long bookerId, Long itemId) {
        List<BookingDto> bookings = bookingService.getByBookerIdAndItemIdAndStatus(bookerId, itemId,
                Status.APPROVED);
        if (bookings.isEmpty()) {
            throw new ValidationException(
                    "Чтобы оставить комментарий, нужно взять вещь в аренду");
        }
        if (!bookings.getFirst().getStatus().equals(Status.PAST)) {
            throw new ValidationException(
                    "Чтобы оставить комментарий, нужно дождаться, пока закончится срок аренды");
        }
    }
}