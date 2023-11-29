package template.tenants.dtos;

import java.util.UUID;

public class TenantUserDto {
    public Integer id;
    public UUID tenantId;
    public String firstName;
    public String lastName;
    public String email;
    public String phone;
    public String createdOn;
    public String updatedOn;
}
