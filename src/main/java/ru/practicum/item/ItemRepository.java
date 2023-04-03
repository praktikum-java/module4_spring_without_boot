package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.item.model.Item;
import ru.practicum.item.model.ItemCountByUser;
import ru.practicum.item.model.ItemInfo;
import ru.practicum.user.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

    List<Item> findByUserId(long userId);

    Optional<Item> findByUserIdAndUrl(long userId, String url);

    List<ItemInfo> findAllByUserId(Long userId);

    Optional<Item> findByUserAndResolvedUrl(User user, String resolvedUrl);

    @Query("select it " +
            "from Item as it " +
            "join it.user as u " +
            "where u.lastName like concat(?1, '%') ")
    List<Item> findItemsByLastNamePrefix(String lastNamePrefix);

    @Query("select new ru.practicum.item.model.ItemCountByUser(it.user.id, count(it.id))" +
            "from Item as it "+
            "where it.url like ?1 "+
            "group by it.user.id "+
            "order by count(it.id) desc")
    List<ItemCountByUser> countItemsByUser(String urlPart);

    @Query(value = "select it.user_id, count(it.id) as count "+
            "from items as it left join users as us on it.user_id = us.id "+
            "where (cast(us.registration_date as date)) between ?1 and ?2 "+
            "group by it.user_id", nativeQuery = true)
    List<ItemCountByUser> countByUserRegistered(LocalDate dateFrom, LocalDate dateTo);

    void deleteByUserIdAndId(long userId, long itemId);
}