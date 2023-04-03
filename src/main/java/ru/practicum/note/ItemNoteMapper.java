package ru.practicum.note;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.item.model.Item;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemNoteMapper {

    public static ItemNoteDto mapToItemNoteDto(ItemNote itemNote) {
        String dateOfNote = DateTimeFormatter
                .ofPattern("yyyy.MM.dd hh:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(itemNote.getDateOfNote());

        return new ItemNoteDto(
                itemNote.getId(),
                itemNote.getItem().getId(),
                itemNote.getText(),
                dateOfNote,
                itemNote.getItem().getUrl()
        );
    }

    public static List<ItemNoteDto> mapToItemNoteDto(Iterable<ItemNote> itemNotes) {
        List<ItemNoteDto> dtos = new ArrayList<>();
        for (ItemNote itemNote : itemNotes) {
            dtos.add(mapToItemNoteDto(itemNote));
        }
        return dtos;
    }

    public static ItemNote mapToItemNote(ItemNoteDto itemNoteDto, Item item) {
        ItemNote itemNote = new ItemNote();
        itemNote.setItem(item);
        itemNote.setText(itemNoteDto.getText());
        return itemNote;
    }
}
