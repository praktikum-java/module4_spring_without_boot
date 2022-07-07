package ru.practicum.item;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

interface ItemService {
    List<ItemDto> getItems(long userId);

    ItemDto addNewItem(long userId, ItemDto itemDto);

    void deleteItem(long userId, long itemId);

    List<ItemInfoWithUrlState> getUserItemStates(long userId);

    @Transactional(readOnly = true)
    List<ItemDto> getItems(long userId, Set<String> tags);
}
