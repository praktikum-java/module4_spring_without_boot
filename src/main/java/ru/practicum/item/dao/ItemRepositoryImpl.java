package ru.practicum.item.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import ru.practicum.item.ItemUrlStatusProvider;
import ru.practicum.item.model.ItemInfo;
import ru.practicum.item.model.ItemInfoWithUrlState;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final ItemRepository itemRepository;

    private final ItemUrlStatusProvider itemUrlStatusProvider;

    public ItemRepositoryImpl(@Lazy ItemRepository itemRepository, @Lazy ItemUrlStatusProvider itemUrlStatusProvider){
        this.itemRepository = itemRepository;
        this.itemUrlStatusProvider = itemUrlStatusProvider;
    }

    @Override
    public List<ItemInfoWithUrlState> findAllByUserIdWithUrlState(Long userId) {
        List<ItemInfo> userUrls = itemRepository.findAllByUserId(userId);
        List<ItemInfoWithUrlState> checkedUrls = userUrls.stream()
                .map(info -> {
                    HttpStatus status = itemUrlStatusProvider.getItemUrlStatus(info.getId());
                    return new ItemInfoWithUrlState(info, status);
                })
                .collect(Collectors.toList());
        return checkedUrls;
    }
}