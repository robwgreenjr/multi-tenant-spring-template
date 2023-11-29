package template.tenants.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.global.utilities.ParameterProcessor;
import template.tenants.dtos.TenantDto;
import template.tenants.entities.TenantEntity;
import template.tenants.mappers.TenantMapper;
import template.tenants.models.Tenant;
import template.tenants.services.TenantManager;

import java.util.UUID;

@RestController
@RequestMapping("internal")
public class TenantController {

    private final TenantManager tenantManager;
    private final TenantMapper tenantMapper;
    private final ParameterProcessor<UUID> parameterSetter;

    public TenantController(TenantManager tenantManager,
                            TenantMapper tenantMapper,
                            ParameterProcessor<UUID> parameterSetter) {
        this.tenantManager = tenantManager;
        this.tenantMapper = tenantMapper;
        this.parameterSetter = parameterSetter;
    }

    @GetMapping("tenants")
    public QueryResult<TenantDto> findAll(HttpServletRequest request) {
        Query<UUID> query =
            parameterSetter.buildquery(request.getParameterMap());

        QueryResult<Tenant> result = tenantManager.getList(query);

        QueryResult<TenantDto> response = new QueryResult<>();
        response.setData(
            tenantMapper.tenantModelListToTenantDtoList(result.getData()));
        response.setMeta(result.getMeta());

        return response;
    }

    @GetMapping("tenant/{id}")
    public QueryResult<TenantDto> find(@PathVariable UUID id,
                                       HttpServletRequest request) {
        Query<UUID> query =
            parameterSetter.buildquery(request.getParameterMap());
        query.setPrimaryId(id);

        QueryResult<Tenant> result = tenantManager.getSingle(query);

        QueryResult<TenantDto> response = new QueryResult<>();
        response.setData(
            tenantMapper.tenantModelListToTenantDtoList(result.getData()));
        response.setMeta(result.getMeta());

        return response;
    }

    @PostMapping("tenant")
    public ResponseEntity<TenantDto> save(@RequestBody TenantEntity tenant)
        throws Exception {
        Tenant tenantModel = tenantMapper.toTenantModel(tenant);

        tenantModel = tenantManager.create(tenantModel);

        TenantDto tenantDto = tenantMapper.tenantModelToTenantDto(tenantModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(tenantDto);
    }

    @PutMapping("tenant/{id}")
    public ResponseEntity<TenantDto> update(@RequestBody TenantEntity tenant,
                                            @PathVariable UUID id)
        throws Exception {
        Tenant tenantModel = tenantMapper.toTenantModel(tenant);

        tenantModel = tenantManager.update(id, tenantModel);

        TenantDto tenantDto = tenantMapper.tenantModelToTenantDto(tenantModel);
        return ResponseEntity.status(HttpStatus.OK).body(tenantDto);
    }

    @PatchMapping("tenant/{id}")
    public ResponseEntity<TenantDto> updatePartial(
        @RequestBody TenantEntity tenant,
        @PathVariable UUID id)
        throws Exception {
        Tenant tenantModel = tenantMapper.toTenantModel(tenant);
        tenantModel = tenantManager.updatePartial(id, tenantModel);

        TenantDto tenantDto = tenantMapper.tenantModelToTenantDto(tenantModel);
        return ResponseEntity.status(HttpStatus.OK).body(tenantDto);
    }

    @DeleteMapping("tenant/{id}")
    public void delete(@PathVariable UUID id) throws Exception {
        tenantManager.delete(id);
    }
}
