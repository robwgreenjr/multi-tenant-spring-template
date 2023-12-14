package template.hypermedia.helpers;

import org.mapstruct.Mapper;
import template.database.helpers.DoubleTable;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DoubleTableMapper {
    List<DoubleTableDto> toDoubleTableDtoList(
        List<DoubleTable> doubleTableList);

    DoubleTableDto toDoubleTableDto(DoubleTable doubleTable);

    default String map(Instant instant) {
        return instant == null ? null : instant.toString();
    }
}
