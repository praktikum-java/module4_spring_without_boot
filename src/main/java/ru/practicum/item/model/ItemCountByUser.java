package ru.practicum.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemCountByUser {

    private Long userId;
    
    private Long count;
    
}