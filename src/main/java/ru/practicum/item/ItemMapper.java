package ru.practicum.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;
import ru.practicum.user.User;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ItemMapper {
    private static final DateTimeFormatter dtFormatter = DateTimeFormatter
            .ofPattern("yyyy.MM.dd hh:mm:ss")
            .withZone(ZoneOffset.UTC);

    public static Item mapToItem(UrlMetaDataRetriever.UrlMetadata result, User user, Set<String> tags) {
        Item item = new Item();
        item.setUser(user);
        item.setUrl(result.getNormalUrl());
        item.setResolvedUrl(result.getResolvedUrl());
        item.setMimeType(result.getMimeType());
        item.setTitle(result.getTitle());
        item.setHasImage(result.isHasImage());
        item.setHasVideo(result.isHasVideo());
        item.setDateResolved(result.getDateResolved());
        item.setTags(tags);
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .title(item.getTitle())
                .normalUrl(item.getUrl())
                .resolvedUrl(item.getResolvedUrl())
                .hasImage(item.isHasImage())
                .hasVideo(item.isHasVideo())
                .mimeType(item.getMimeType())
                .unread(item.isUnread())
                .dateResolved(dtFormatter.format(item.getDateResolved()))
                // Нужно скопировать все элементы в новую коллекцию - чтобы запустить механизм ленивой загрузки.
                .tags(new HashSet<>(item.getTags()))
                .build();
    }

    public static List<ItemDto> mapToItemDto(Iterable<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();
        for (Item item : items) {
            dtos.add(mapToItemDto(item));
        }
        return dtos;
    }
}