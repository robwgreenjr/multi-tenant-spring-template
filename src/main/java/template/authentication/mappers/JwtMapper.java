package template.authentication.mappers;

import org.mapstruct.Mapper;
import template.authentication.dtos.JwtDto;
import template.authentication.models.Jwt;

@Mapper(componentModel = "spring")
public interface JwtMapper {
    JwtDto jwtModelToJwtDto(Jwt jwtModel);
}