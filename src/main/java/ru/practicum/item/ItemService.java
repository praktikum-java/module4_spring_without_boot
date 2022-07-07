package ru.practicum.item;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
interface ItemService {
    List<ItemDto> getItems(long userId);

    @Transactional
    ItemDto addNewItem(long userId, ItemDto itemDto);

    @Transactional
    void deleteItem(long userId, long itemId);

    @Transactional(readOnly = true)
    List<ItemDto> getItems(long userId, Set<String> tags);

    @Transactional(readOnly = true)
    List<ItemDto> getUserItems(String lastName);
}
