package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item getItemById(long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public Item create(Item item) {
        checkUser(item);
        return itemRepository.create(item);
    }

    @Override
    public Item update(Item item) {
        checkUser(item);
        return itemRepository.update(item);
    }

    @Override
    public List<Item> getItemsByOwnerId(long ownerId) {
        return itemRepository.getItemsByOwnerId(ownerId);
    }

    @Override
    public List<Item> getItemsByNameOrDescription(String text) {
        if (text == null || text.isEmpty()) {
            return List.of();
        }
        return itemRepository.getItemsByNameOrDescription(text.toLowerCase());
    }

    private void checkUser(Item item) {
        if (userRepository.getUserById(item.getOwnerId()) == null) {
            throw new NotFoundException("User отсутствует");
        }
    }
}