package ru.practicum.item;

import java.time.Instant;

interface UrlMetaDataRetriever {
    UrlMetadata retrieve(String uri);

    // Закрепляем в виде вложенного интерфейса контракт
    // об обязательных данных, которые должны возвращать
    // все реализации UrlMetaDataRetriever'а.
    // Отдельные реализации могут дополнять метаданные
    // новой информацией (расширяя своими интерфейсами
    // или сразу добавляя в свои реализации UrlMetadata),
    // но описанное здесь, должно быть у всех реализаций.
    interface UrlMetadata {
        String getNormalUrl();
        String getResolvedUrl();
        String getMimeType();
        String getTitle();
        boolean isHasImage();
        boolean isHasVideo();
        Instant getDateResolved();
    }
}
