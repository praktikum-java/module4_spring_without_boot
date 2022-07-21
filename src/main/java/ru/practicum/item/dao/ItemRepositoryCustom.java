package ru.practicum.item.dao;

import ru.practicum.item.model.ItemInfoWithUrlState;

import java.util.List;

public interface ItemRepositoryCustom {
    List<ItemInfoWithUrlState> findAllByUserIdWithUrlState(Long userId);
}