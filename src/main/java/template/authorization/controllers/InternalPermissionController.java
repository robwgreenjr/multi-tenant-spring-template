package template.authorization.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import template.authorization.dtos.InternalPermissionDto;
import template.authorization.mappers.InternalPermissionMapper;
import template.authorization.models.InternalPermission;
import template.authorization.services.InternalPermissionManager;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.global.utilities.ParameterProcessor;

import java.util.List;

@RestController
@RequestMapping("internal/authorization")
public class InternalPermissionController {
    private final InternalPermissionManager permissionManager;
    private final InternalPermissionMapper permissionMapper;
    private final ParameterProcessor<Integer> parameterSetter;

    public InternalPermissionController(
        InternalPermissionManager permissionManager,
        InternalPermissionMapper permissionMapper,
        ParameterProcessor<Integer> parameterSetter) {
        this.permissionManager = permissionManager;
        this.permissionMapper = permissionMapper;
        this.parameterSetter = parameterSetter;
    }

    @GetMapping("permissions")
    public QueryResult<InternalPermissionDto> getList(
        HttpServletRequest request)
        throws Exception {
        Query<Integer> query =
            parameterSetter.buildQuery(request.getParameterMap());

        QueryResult<InternalPermission> result =
            permissionManager.getList(query);

        QueryResult<InternalPermissionDto> response = new QueryResult<>();
        response.setData(permissionMapper.toDtoList(result.getData()));
        response.setMeta(result.getMeta());

        return response;
    }

    @GetMapping("permission/{id}")
    public QueryResult<InternalPermissionDto> find(@PathVariable Integer id,
                                                   HttpServletRequest request) {
        Query<Integer> query =
            parameterSetter.buildQuery(request.getParameterMap());
        query.setPrimaryId(id);

        QueryResult<InternalPermission> result =
            permissionManager.getSingle(query);

        QueryResult<InternalPermissionDto> response = new QueryResult<>();
        response.setData(permissionMapper.toDtoList(result.getData()));
        response.setMeta(result.getMeta());

        return response;
    }

    @PostMapping("permission")
    public ResponseEntity<InternalPermissionDto> save(
        @RequestBody InternalPermissionDto permissionDto)
        throws Exception {
        InternalPermission permissionModel =
            permissionMapper.dtoToObject(permissionDto);

        permissionModel = permissionManager.create(permissionModel);

        permissionDto = permissionMapper.toDto(permissionModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionDto);
    }

    @PostMapping("permissions")
    public ResponseEntity<List<InternalPermissionDto>> saveAll(
        @RequestBody List<InternalPermissionDto> permissionDtoList) {
        List<InternalPermission> permissionModelList =
            permissionMapper.dtoToList(permissionDtoList);

        List<InternalPermission> result =
            permissionManager.createAll(permissionModelList);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(permissionMapper.toDtoList(result));
    }

    @PutMapping("permissions")
    public List<InternalPermissionDto> updateAll(
        @RequestBody List<InternalPermissionDto> permissionDtoList)
        throws Exception {
        List<InternalPermission> permissionModelList =
            permissionMapper.dtoToList(permissionDtoList);

        List<InternalPermission> result =
            permissionManager.updateAll(permissionModelList);

        return permissionMapper.toDtoList(result);
    }

    @PutMapping("permission/{id}")
    public ResponseEntity<InternalPermissionDto> update(
        @RequestBody InternalPermissionDto permissionDto,
        @PathVariable Integer id)
        throws Exception {
        InternalPermission permissionModel =
            permissionMapper.dtoToObject(permissionDto);

        permissionModel = permissionManager.update(id, permissionModel);

        permissionDto = permissionMapper.toDto(
            permissionModel);
        return ResponseEntity.status(HttpStatus.OK).body(permissionDto);
    }

    @PatchMapping("permission/{id}")
    public ResponseEntity<InternalPermissionDto> updatePartial(
        @RequestBody InternalPermissionDto permissionDto,
        @PathVariable Integer id)
        throws Exception {
        InternalPermission permissionModel =
            permissionMapper.dtoToObject(permissionDto);

        permissionModel = permissionManager.updatePartial(id, permissionModel);

        permissionDto = permissionMapper.toDto(permissionModel);
        return ResponseEntity.status(HttpStatus.OK).body(permissionDto);
    }

    @DeleteMapping("permission/{id}")
    public void delete(@PathVariable Integer id) throws Exception {
        permissionManager.delete(id);
    }
}