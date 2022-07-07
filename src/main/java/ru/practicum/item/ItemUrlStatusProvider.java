package ru.practicum.item;

import org.springframework.http.HttpStatus;

public interface ItemUrlStatusProvider {

    HttpStatus getItemUrlStatus(Long itemId);
}