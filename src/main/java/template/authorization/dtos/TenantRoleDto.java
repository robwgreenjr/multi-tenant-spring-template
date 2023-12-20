package template.authorization.dtos;

import template.tenants.dtos.TenantUserDto;

import java.util.Set;

public class TenantRoleDto {
    public String id;
    public String name;
    public String description;
    public Set<InternalPermissionDto> permissions;
    public Set<TenantUserDto> users;
}