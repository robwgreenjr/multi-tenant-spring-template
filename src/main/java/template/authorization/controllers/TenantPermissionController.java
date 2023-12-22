package template.authorization.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import template.authorization.dtos.TenantPermissionDto;
import template.authorization.mappers.TenantPermissionMapper;
import template.authorization.models.TenantPermission;
import template.authorization.services.TenantPermissionManager;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.global.utilities.ParameterProcessor;

import java.util.List;

@RestController
@RequestMapping("authorization")
public class TenantPermissionController {
    private final TenantPermissionManager permissionManager;
    private final TenantPermissionMapper permissionMapper;
    private final ParameterProcessor<Integer> parameterSetter;

    public TenantPermissionController(
        TenantPermissionManager permissionManager,
        TenantPermissionMapper permissionMapper,
        ParameterProcessor<Integer> parameterSetter) {
        this.permissionManager = permissionManager;
        this.permissionMapper = permissionMapper;
        this.parameterSetter = parameterSetter;
    }

    @GetMapping("permissions")
    public QueryResult<TenantPermissionDto> getList(
        HttpServletRequest request)
        throws Exception {
        Query<Integer> query =
            parameterSetter.buildQuery(request.getParameterMap());

        QueryResult<TenantPermission> result =
            permissionManager.getList(query);

        QueryResult<TenantPermissionDto> response = new QueryResult<>();
        response.setData(permissionMapper.toDtoList(result.getData()));
        response.setMeta(result.getMeta());

        return response;
    }

    @GetMapping("permission/{id}")
    public QueryResult<TenantPermissionDto> find(@PathVariable Integer id,
                                                 HttpServletRequest request) {
        Query<Integer> query =
            parameterSetter.buildQuery(request.getParameterMap());
        query.setPrimaryId(id);

        QueryResult<TenantPermission> result =
            permissionManager.getSingle(query);

        QueryResult<TenantPermissionDto> response = new QueryResult<>();
        response.setData(permissionMapper.toDtoList(result.getData()));
        response.setMeta(result.getMeta());

        return response;
    }

    @PostMapping("permission")
    public ResponseEntity<TenantPermissionDto> save(
        @RequestBody TenantPermissionDto permissionDto)
        throws Exception {
        TenantPermission permission =
            permissionMapper.dtoToObject(permissionDto);

        permission = permissionManager.create(permission);

        permissionDto = permissionMapper.toDto(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionDto);
    }

    @PostMapping("permissions")
    public ResponseEntity<List<TenantPermissionDto>> saveAll(
        @RequestBody List<TenantPermissionDto> permissionDtoList) {
        List<TenantPermission> permissionList =
            permissionMapper.dtoToList(permissionDtoList);

        List<TenantPermission> result =
            permissionManager.createAll(permissionList);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(permissionMapper.toDtoList(result));
    }

    @PutMapping("permissions")
    public List<TenantPermissionDto> updateAll(
        @RequestBody List<TenantPermissionDto> permissionDtoList)
        throws Exception {
        List<TenantPermission> permissionList =
            permissionMapper.dtoToList(permissionDtoList);

        List<TenantPermission> result =
            permissionManager.updateAll(permissionList);

        return permissionMapper.toDtoList(result);
    }

    @PutMapping("permission/{id}")
    public ResponseEntity<TenantPermissionDto> update(
        @RequestBody TenantPermissionDto permissionDto,
        @PathVariable Integer id)
        throws Exception {
        TenantPermission permission =
            permissionMapper.dtoToObject(permissionDto);

        permission = permissionManager.update(id, permission);

        permissionDto = permissionMapper.toDto(permission);
        return ResponseEntity.status(HttpStatus.OK).body(permissionDto);
    }

    @PatchMapping("permission/{id}")
    public ResponseEntity<TenantPermissionDto> updatePartial(
        @RequestBody TenantPermissionDto permissionDto,
        @PathVariable Integer id)
        throws Exception {
        TenantPermission permission =
            permissionMapper.dtoToObject(permissionDto);

        permission = permissionManager.updatePartial(id, permission);

        permissionDto = permissionMapper.toDto(permission);
        return ResponseEntity.status(HttpStatus.OK).body(permissionDto);
    }

    @DeleteMapping("permission/{id}")
    public void delete(@PathVariable Integer id) throws Exception {
        permissionManager.delete(id);
    }
}