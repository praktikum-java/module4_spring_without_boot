package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ModifyItemRequest {
    private long itemId;
    private boolean read;
    private Set<String> tags;
    private boolean replaceTags;

    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }
}
