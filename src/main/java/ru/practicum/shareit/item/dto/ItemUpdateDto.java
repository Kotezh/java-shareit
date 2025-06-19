package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ItemUpdateDto {
    private String name;
    private String description;
    private Boolean available;
}
