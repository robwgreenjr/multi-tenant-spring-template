package template.hypermedia.helpers;

import template.database.helpers.SingleTable;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SingleTableMapper {
    List<SingleTableDto> toSingleTableDtoList(
        List<SingleTable> singleTableList);

    SingleTableDto toSingleTableDto(SingleTable singleTable);

    default String map(Instant instant) {
        return instant == null ? null : instant.toString();
    }
}
