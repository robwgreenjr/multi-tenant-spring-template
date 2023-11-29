package template.authorization.dtos;

import template.internal.dtos.InternalUserDto;

import java.util.Set;

public class InternalRoleDto {
    public Integer id;
    public String name;
    public String description;
    public Set<InternalPermissionDto> permissions;
    public Set<InternalUserDto> users;
}