package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final String CUSTOM_HEADER = "X-Sharer-User-Id";

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        return itemMapper.mapModelToDto(itemService.getItemById(itemId));
    }

    @PostMapping
    public ItemDto create(@RequestHeader(CUSTOM_HEADER) Long userId,
                          @Validated @RequestBody ItemCreateDto createDto) {
        Item item = itemService.create(itemMapper.createDtoToModel(createDto, userId));
        return itemMapper.mapModelToDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(CUSTOM_HEADER) Long userId, @PathVariable Long itemId,
                          @Validated @RequestBody ItemUpdateDto updateDto) {
        Item item = itemService.update(itemMapper.updateDtoToModel(updateDto, userId, itemId));
        return itemMapper.mapModelToDto(item);
    }

    @GetMapping
    public List<ItemDto> getItemByOwnerId(@RequestHeader(CUSTOM_HEADER) Long userId) {
        return itemMapper.mapListModelToDto(itemService.getItemsByOwnerId(userId));
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByNameOrDescription(@RequestParam String text) {
        return itemMapper.mapListModelToDto(itemService.getItemsByNameOrDescription(text));
    }
}
