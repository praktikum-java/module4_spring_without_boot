package ru.practicum.item;

import java.util.List;

public interface ItemRepositoryCustom {
    List<ItemInfoWithUrlState> findAllByUserIdWithUrlState(Long userId);
}