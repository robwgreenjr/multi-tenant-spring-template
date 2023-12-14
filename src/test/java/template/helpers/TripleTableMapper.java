package template.helpers;

import org.mapstruct.Mapper;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TripleTableMapper {
    List<TripleTableDto> toTripleTableDtoList(
        List<TripleTable> tripleTableList);

    TripleTableDto toTripleTableDto(TripleTable tripleTable);

    default String map(Instant instant) {
        return instant == null ? null : instant.toString();
    }
}
