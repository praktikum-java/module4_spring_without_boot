package ru.practicum.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.InsufficientPermissionException;
import ru.practicum.common.NotFoundException;
import ru.practicum.item.dto.AddItemRequest;
import ru.practicum.item.dto.GetItemRequest;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ModifyItemRequest;
import ru.practicum.item.model.Item;
import ru.practicum.item.model.QItem;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final UrlMetaDataRetriever urlMetaDataRetriever;

    @Override
    public List<ItemDto> getItems(long userId) {
        List<Item> userItems = repository.findByUserId(userId);
        return ItemMapper.mapToItemDto(userItems);
    }

    @Transactional
    @Override
    public ItemDto addNewItem(Long userId, AddItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InsufficientPermissionException("You do not have permission to perform this operation"));

        // Собираем метаданные о переданном url-адресе
        UrlMetaDataRetriever.UrlMetadata result = urlMetaDataRetriever.retrieve(request.getUrl());

        // проверяем, возможно такой url-адрес уже был сохранён
        // если адрес уже есть, то обновляем информацию о тегах и возвращаем обновлённый item
        // в противном случае - сохраняем новый item и возвращаем его
        Optional<Item> maybeExistingItem = repository.findByUserAndResolvedUrl(user, result.getResolvedUrl());
        Item item;
        if(maybeExistingItem.isEmpty()) {
            item = repository.save(ItemMapper.mapToItem(result, user, request.getTags()));
        } else {
            item = maybeExistingItem.get();
            if(request.getTags() != null && !request.getTags().isEmpty()) {
                item.getTags().addAll(request.getTags());
                repository.save(item);
            }
        }
        return ItemMapper.mapToItemDto(item);
    }

    @Transactional
    @Override
    public void deleteItem(long userId, long itemId) {
        repository.deleteByUserIdAndId(userId, itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItems(GetItemRequest req) {
        // Для поиска ссылок используем QueryDSL чтобы было удобно настраивать разные варианты фильтров
        QItem item = QItem.item;
        // Мы будем анализировать какие фильтры указал пользователь
        // И все нужные условия фильтрации будем собирать в список
        List<BooleanExpression> conditions = new ArrayList<>();
        // Условие, которое будет проверяться всегда - пользователь сделавший запрос
        // должен быть тем же пользователем, что сохранил ссылку
        conditions.add(item.user.id.eq(req.getUserId()));

        // Проверяем один из фильтров указанных в запросе - state
        GetItemRequest.State state = req.getState();
        // Если пользователь указал, что его интересуют все ссылки, вне зависимости
        // от состояния, тогда пропускаем этот фильтр. В обратном случае анализируем
        // указанное состояние и формируем подходящее условие для запроса
        if(!state.equals(GetItemRequest.State.ALL)) {
            conditions.add(makeStateCondition(state));
        }

        // Если пользователь указал, что его интересуют ссылки вне зависимости
        // от типа их содержимого, то пропускаем фильтра, иначе анализируем
        // указанный тип контента и формируем соответствующее условие
        GetItemRequest.ContentType contentType = req.getContentType();
        if(!contentType.equals(GetItemRequest.ContentType.ALL)) {
            conditions.add(makeContentTypeCondition(contentType));
        }

        // если пользователя интересуют ссылки с конкретными тэгами,
        // то добавляем это условие в запрос
        if(req.hasTags()) {
            conditions.add(item.tags.any().in(req.getTags()));
        }

        // из всех подготовленных условий, составляем единое условие
        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        // анализируем, какой вариант сортировки выбрал пользователь
        // и какое количество элементов он выбрал для отображения
        Sort sort = makeOrderByClause(req.getSort());
        PageRequest pageRequest = PageRequest.of(0, req.getLimit(), sort);

        // выполняем запрос к базе данных со всеми подготовленными настройками
        // конвертируем результат в DTO и возвращаем контроллеру
        Iterable<Item> items = repository.findAll(finalCondition, pageRequest);
        return ItemMapper.mapToItemDto(items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getUserItems(String lastName) {
        List<Item> foundItems = repository.findItemsByLastNamePrefix(lastName);
        return ItemMapper.mapToItemDto(foundItems);
    }

    @Override
    public ItemDto changeItem(long userId, ModifyItemRequest request) {
        Optional<Item> maybeItem = getAndCheckPermissions(userId, request.getItemId());
        if(maybeItem.isPresent()) {
            Item item = maybeItem.get();

            item.setUnread(!request.isRead());

            // Если существующий набор тегов нужно заменить новым (в том числе пустым),
            // то предварительно нужно очистить коллекцию тегов
            if(request.isReplaceTags()) {
                item.getTags().clear();
            }
            // Добавляем переданные теги
            if(request.hasTags()) {
                item.getTags().addAll(request.getTags());
            }
            item = repository.save(item);
            return ItemMapper.mapToItemDto(item);
        } else {
            throw new NotFoundException("The item with id " + request.getItemId() + " was not found");
        }
    }

    private Optional<Item> getAndCheckPermissions(long userId, long itemId) {
        Optional<Item> maybeItem = repository.findById(itemId);
        if (maybeItem.isPresent()) {
            Item item = maybeItem.get();
            if(!item.getUser().getId().equals(userId)) {
                throw new InsufficientPermissionException("You do not have permission to perform this operation");
            }
        }
        return maybeItem;
    }

    private BooleanExpression makeStateCondition(GetItemRequest.State state) {
        if(state.equals(GetItemRequest.State.READ)) {
            return QItem.item.unread.isFalse();
        } else {
            return QItem.item.unread.isTrue();
        }
    }

    private BooleanExpression makeContentTypeCondition(GetItemRequest.ContentType contentType) {
        if(contentType.equals(GetItemRequest.ContentType.ARTICLE)) {
            return QItem.item.mimeType.eq("text");
        } else if(contentType.equals(GetItemRequest.ContentType.IMAGE)) {
            return QItem.item.mimeType.eq("image");
        } else {
            return QItem.item.mimeType.eq("video");
        }
    }

    private Sort makeOrderByClause(GetItemRequest.Sort sort) {
        switch (sort) {
            case TITLE: return Sort.by("title").ascending();
            case SITE: return Sort.by("resolvedUrl").ascending();
            case OLDEST: return Sort.by("dateResolved").ascending();
            case NEWEST:
            default: return Sort.by("dateResolved").descending();
        }
    }
}
