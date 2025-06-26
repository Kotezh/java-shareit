package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@UtilityClass
public class ItemMapper {

    public static Item mapToItem(ItemDto dto) {
        return Item.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .ownerId(dto.getOwnerId())
                .requestId(dto.getRequestId())
                .build();
    }

    public static ItemDto mapToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .requestId(item.getRequestId())
                .build();
    }

    public static List<Item> mapToItems(List<ItemDto> itemsDto) {
        return itemsDto.stream().map(ItemMapper::mapToItem).toList();
    }

    public static List<ItemDto> mapToDtoList(List<Item> items) {
        return items.stream().map(ItemMapper::mapToDto).toList();
    }

}