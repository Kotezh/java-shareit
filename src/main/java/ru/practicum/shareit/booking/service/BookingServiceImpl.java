package ru.practicum.shareit.booking.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingDto get(Long id) {
        return getImpl(id);
    }

    @Override
    @Transactional
    public BookingDto create(BookingDto bookingDto, Long userId) {
        checkItemAndUserExist(bookingDto.getItemId(), userId);

        Booking bookingNew = BookingMapper.mapToBooking(bookingDto);
        bookingNew.setBookerId(userId);
        bookingNew.setStatus(Status.WAITING);
        BookingDto res = BookingMapper.mapToDto(bookingRepository.save(bookingNew));

        return addBookingInfo(res);
    }

    @Override
    @Transactional
    public BookingDto update(Long bookingId, BookingDto booking, Long userId) {
        Booking bookingSaved = checkAccessForUpdateAndGetBooking(bookingId, userId);

        if (booking.getStatus() != null) {
            bookingSaved.setStatus(booking.getStatus());
        }

        return BookingMapper.mapToDto(bookingRepository.save(bookingSaved));
    }

    @Override
    @Transactional
    public BookingDto updateStatus(Long bookingId, Long userId, boolean approved) {
        Booking bookingSaved = checkAccessForStatusChangeAndGetBooking(bookingId, userId);
        Status status = approved ? Status.APPROVED : Status.REJECTED;
        bookingSaved.setStatus(status);
        BookingDto res = BookingMapper.mapToDto(bookingRepository.save(bookingSaved));
        return addBookingInfo(res);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<BookingDto> getByBookerId(Long userId, Status status) {
        List<BookingDto> res;

        if (status.equals(Status.ALL)) {
            res = BookingMapper.mapToDtoList(bookingRepository.getByBookerId(userId));
        } else {
            res = BookingMapper.mapToDtoList(bookingRepository.getByBookerIdAndStatus(userId, status));
        }

        return addBookingListInfo(res);
    }

    @Override
    public List<BookingDto> getByBookerIdAndItemIdAndStatus(Long userId, Long itemId, Status status) {
        List<BookingDto> res = BookingMapper
                .mapToDtoList(bookingRepository.getByBookerIdAndItemIdAndStatus(userId, itemId, status));

        return setStatusPast(res);
    }

    @Override
    public List<BookingDto> getByOwnerId(Long userId, Status status) {
        if (userId == null || !userService.checkUserId(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        List<Long> itemsIds = itemService.getItemsByOwnerId(userId).stream().map(ItemDto::getId).toList();
        return BookingMapper.mapToDtoList(bookingRepository.getByItemIdInAndStatus(itemsIds, status));
    }

    @Override
    public boolean isIdExist(Long id) {
        return bookingRepository.existsById(id);
    }

    @Override
    public boolean isItemOfBookerWithStatus(Long bookerId, Long itemId, Status status) {
        return bookingRepository.checkItemOfBookerWithStatus(bookerId, itemId, status.ordinal());
    }

    private void checkItemAndUserExist(Long itemId, Long userId) {
        if (!itemService.isIdExist(itemId)) {
            throw new NotFoundException("Вещь с id " + itemId + " не найдена");
        }
        if (!userService.checkUserId(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (!itemService.isAvailable(itemId)) {
            throw new ValidationException("Эту вещь с id "  + itemId + " сейчас невозможно забронировать");
        }
    }

    private Booking checkBookingAndUserExistReturnBooking(Long bookingId, Long userId) {
        if (bookingId == null || !isIdExist(bookingId)) {
            throw new NotFoundException("Бронь с таким id " + bookingId + " не найдена");
        }

        if (userId == null || !userService.checkUserId(userId)) {
            throw new AccessDeniedException("Пользователь с таким id "  + userId + " не найден");
        }

        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь с таким id " + bookingId + " не найдена"));

    }

    private Booking checkAccessForUpdateAndGetBooking(Long bookingId, Long userId) {
        Booking bookingSaved = checkBookingAndUserExistReturnBooking(bookingId, userId);

        if (!bookingSaved.getBookerId().equals(userId)) {
            throw new AccessDeniedException("Только владелец заказа может редактировать заказ");
        }

        return bookingSaved;
    }

    private Booking checkAccessForStatusChangeAndGetBooking(Long bookingId, Long userId) {
        Booking bookingSaved = checkBookingAndUserExistReturnBooking(bookingId, userId);
        Long itemOwnerId = itemService.get(bookingSaved.getItemId()).getOwnerId();

        if (!itemOwnerId.equals(userId)) {
            throw new AccessDeniedException("Только владелец вещи может одобрить заказ");
        }

        return bookingSaved;
    }

    private BookingDto getImpl(Long id) {
        BookingDto booking = bookingRepository.findById(id)
                .map(BookingMapper::mapToDto)
                .orElseThrow(() -> new NotFoundException("Бронь с таким id " + bookingId + " не найдена"));

        return setStatusPast(addBookingInfo(booking));
    }

    private List<BookingDto> addBookingListInfo(List<BookingDto> bookings) {
        List<Long> itemsIds = bookings.stream().map(BookingDto::getItemId).toList();
        List<Long> usersIds = bookings.stream().map(BookingDto::getBookerId).toList();

        List<ItemDto> items = itemService.getByIds(itemsIds);
        List<UserDto> users = userService.getUsers(usersIds);

        Map<Long, ItemDto> itemsByIds = items.stream()
                .collect(Collectors.toMap(ItemDto::getId, Function.identity()));

        Map<Long, UserDto> usersByIds = users.stream()
                .collect(Collectors.toMap(UserDto::getId, Function.identity()));

        return bookings.stream()
                .peek(x -> {
                    x.setItem(itemsByIds.getOrDefault(x.getItemId(), null));
                    x.setBooker(usersByIds.getOrDefault(x.getBookerId(), null));
                }).toList();
    }

    private BookingDto addBookingInfo(BookingDto booking) {
        return addBookingListInfo(List.of(booking)).getFirst();
    }

    private List<BookingDto> setStatusPast(List<BookingDto> bookings) {
        return bookings.stream().peek(x -> {
            if (x.getEnd().isBefore(LocalDateTime.now())) {
                x.setStatus(Status.PAST);
            }
        }).toList();
    }

    private BookingDto setStatusPast(BookingDto booking) {
        return setStatusPast(List.of(booking)).getFirst();
    }
}