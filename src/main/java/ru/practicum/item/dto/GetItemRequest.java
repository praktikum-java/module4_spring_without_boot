package ru.practicum.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetItemRequest {
    private long userId;
    private State state;
    private ContentType contentType;
    private Sort sort;
    private Integer limit;
    private List<String> tags;

    public static GetItemRequest of(long userId,
                                    String state,
                                    String contentType,
                                    String sort,
                                    int limit,
                                    List<String> tags) {
        GetItemRequest request = new GetItemRequest();
        request.setUserId(userId);
        request.setLimit(limit);
        request.setState(State.valueOf(state.toUpperCase()));
        request.setContentType(ContentType.valueOf(contentType.toUpperCase()));
        request.setSort(Sort.valueOf(sort.toUpperCase()));
        if(tags != null) {
            request.setTags(tags);
        }
        return request;
    }

    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }

    public enum State {UNREAD, READ, ALL }
    public enum ContentType { ARTICLE, VIDEO, IMAGE, ALL }
    public enum Sort { NEWEST, OLDEST, TITLE, SITE }
}
