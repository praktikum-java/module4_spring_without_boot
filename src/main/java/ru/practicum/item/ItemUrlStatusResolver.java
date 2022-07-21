package ru.practicum.item;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.item.dao.ItemRepository;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ItemUrlStatusResolver implements ItemUrlStatusProvider {

    private static final Map<Long, HttpStatus> urlStatusCache = new ConcurrentHashMap<>();

    private final ItemRepository itemRepository;

    private final RestTemplate restTemplate;

    public ItemUrlStatusResolver(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public HttpStatus getItemUrlStatus(Long itemId) {
        return urlStatusCache.computeIfAbsent(itemId, __ -> HttpStatus.NOT_FOUND);
    }

    public HttpStatus getStatus(String url){
        HttpStatus status;
        try {
            RequestEntity<byte[]> request = new RequestEntity<>(HttpMethod.GET, new URI(url));
            ResponseEntity<byte[]> response = restTemplate.exchange(request, byte[].class);
            status = response.getStatusCode();
        } catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return status;
    }

    public void checkup(){
        itemRepository.findAll().forEach(item ->
                urlStatusCache.put(item.getId(), getStatus(item.getUrl()))
        );
    }

    @PostConstruct
    public void init(){
        checkup();
    }
}