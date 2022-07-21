package ru.practicum.item;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.dto.AddItemRequest;
import ru.practicum.item.dto.GetItemRequest;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ModifyItemRequest;
import ru.practicum.item.model.ItemInfoWithUrlState;

import java.util.List;
import java.util.Set;

interface ItemService {
    List<ItemDto> getItems(long userId);

    ItemDto addNewItem(Long userId, AddItemRequest request);

    void deleteItem(long userId, long itemId);

    List<ItemInfoWithUrlState> getUserItemStates(long userId);


    List<ItemDto> getItems(GetItemRequest req);

    ItemDto changeItem(long userId, ModifyItemRequest request);
}
