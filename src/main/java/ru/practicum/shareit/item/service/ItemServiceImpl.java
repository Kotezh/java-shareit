package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final CommentService commentService;

    @Override
    public ItemDto get(Long itemId) {
        ItemDto itemDto = itemRepository.findById(itemId)
                .map(ItemMapper::mapToDto)
                .orElseThrow(() -> new NotFoundException("Вещь с таким id не найдена"));
        return addComments(itemDto);
    }

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, Long userId) {
        if (!userService.checkUserId(userId)) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        Item item = ItemMapper.mapToItem(itemDto);
        return ItemMapper.mapToDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, Long userId) {
        Item itemSaved = checkAccess(itemDto, userId);

        if (itemDto.getName() != null) {
            itemSaved.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            itemSaved.setDescription(itemDto.getDescription());
        }

        if (itemDto.getRequestId() != null) {
            itemSaved.setRequestId(itemDto.getRequestId());
        }

        if (itemDto.getAvailable() != null) {
            itemSaved.setAvailable(itemDto.getAvailable());
        }

        if (itemDto.getOwnerId() != null) {
            itemSaved.setOwnerId(itemDto.getOwnerId());
        }

        return ItemMapper.mapToDto(itemRepository.save(itemSaved));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> getByIds(List<Long> ids) {
        return itemRepository.getByIds(ids).stream().map(ItemMapper::mapToDto).toList();
    }

    @Override
    public List<ItemDto> getItemsByOwnerId(Long ownerId) {
        return addCommentsAndBookings(ItemMapper.mapToDtoList(itemRepository.findByOwnerId(ownerId)));
    }


    @Override
    public List<ItemDto> getItemsByNameOrDescription(String text) {
        if (text == null || text.isEmpty()) {
            return List.of();
        }
        List<ItemDto> res = ItemMapper.mapToDtoList(itemRepository.findByNameAndDescriptionAndAvailable(text, text));
        return addCommentsAndBookings(res);
    }

    @Override
    @Transactional
    public boolean isAvailable(Long itemId) {
        return itemRepository.findAvailableByItemId(itemId);
    }

    @Override
    public boolean isIdExist(Long itemId) {
        return itemRepository.existsById(itemId);
    }

    private Item checkAccess(ItemDto item, Long userId) {
        Long itemId = item.getId();

        if (!isIdExist(itemId)) {
            throw new NotFoundException("Вещь с таким id не найдена");
        }

        if (!userService.checkUserId(userId)) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }

        Item itemSaved = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с таким id не найдена")
        );

        if (itemSaved.getOwnerId() == null || !itemSaved.getOwnerId().equals(userId)) {
            throw new AccessDeniedException("Только владелец может редактировать вещь");
        }

        return itemSaved;
    }

    private List<ItemDto> addCommentsAndBookings(List<ItemDto> itemsDto) {
        List<Long> itemsIds = itemsDto.stream().map(ItemDto::getId).toList();
        List<CommentDto> comments = commentService.findByItemIds(itemsIds);

        Map<Long, List<CommentDto>> itemComments = comments.stream()
                .collect(Collectors.groupingBy(CommentDto::getItemId));

        return itemsDto.stream()
                .peek(item -> {
                    List<CommentDto> c = itemComments.getOrDefault(item.getId(), List.of());
                    item.setComments(c);
                }).toList();
    }

    private ItemDto addComments(ItemDto itemDto) {
        return addCommentsAndBookings(List.of(itemDto)).getFirst();
    }
}