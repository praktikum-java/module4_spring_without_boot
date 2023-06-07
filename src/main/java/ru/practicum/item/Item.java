package ru.practicum.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "items")
@Getter
@Setter
@ToString
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    // исключаем все поля с отложенной загрузкой из
    // метода toString, чтобы не было случайных обращений
    // базе данных, например при выводе в лог.
    @ToString.Exclude
    private User user;

    @Column
    private String url;
    // здесь остальные поля

    @ElementCollection
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "name")
    private Set<String> tags = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return id != null && id.equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}