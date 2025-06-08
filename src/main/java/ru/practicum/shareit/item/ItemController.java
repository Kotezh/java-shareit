package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
    private final String customHeader = "X-Sharer-User-Id";

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable @Positive Long itemId) {
        return itemMapper.mapModelToDto(itemService.getItemById(itemId));
    }

    @PostMapping
    public ItemDto create(@RequestHeader(customHeader) @Positive Long userId,
                          @Valid @RequestBody ItemCreateDto createDto) {
        Item item = itemService.create(itemMapper.createDtoToModel(createDto, userId));
        return itemMapper.mapModelToDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(customHeader) Long userId, @PathVariable @Positive Long itemId,
                          @Valid @RequestBody ItemUpdateDto updateDto) {
        Item item = itemService.update(itemMapper.updateDtoToModel(updateDto, userId, itemId));
        return itemMapper.mapModelToDto(item);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwnerId(@RequestHeader(customHeader) @Positive Long userId) {
        return itemMapper.mapListModelToDto(itemService.getItemsByOwnerId(userId));
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByNameOrDescription(@RequestParam String text) {
        return itemMapper.mapListModelToDto(itemService.getItemsByNameOrDescription(text));
    }
}
