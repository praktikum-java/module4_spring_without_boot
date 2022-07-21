package ru.practicum.item;

import ru.practicum.common.LaterApplicationException;

public class ItemRetrieverException extends LaterApplicationException {
    public ItemRetrieverException(String message) {
        super(message);
    }

    public ItemRetrieverException(String message, Throwable cause) {
        super(message, cause);
    }
}