package ru.practicum.item;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.dto.AddItemRequest;
import ru.practicum.item.dto.GetItemRequest;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ModifyItemRequest;

import java.util.List;

@Transactional(readOnly = true)
interface ItemService {
    List<ItemDto> getItems(long userId);

    @Transactional
    ItemDto addNewItem(Long userId, AddItemRequest request);

    @Transactional
    void deleteItem(long userId, long itemId);


    List<ItemDto> getItems(GetItemRequest req);

    ItemDto changeItem(long userId, ModifyItemRequest request);

    @Transactional(readOnly = true)
    List<ItemDto> getUserItems(String lastName);
}
