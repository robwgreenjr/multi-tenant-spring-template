package template.authorization.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import template.authorization.dtos.TenantRoleDto;
import template.authorization.mappers.TenantRoleMapper;
import template.authorization.models.TenantRole;
import template.authorization.services.TenantRoleManagerImpl;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.global.utilities.ParameterProcessor;

import java.util.List;

@RestController
@RequestMapping("authorization")
public class TenantRoleController {
    private final TenantRoleManagerImpl roleManager;
    private final TenantRoleMapper roleMapper;
    private final ParameterProcessor<Integer> parameterSetter;

    public TenantRoleController(TenantRoleManagerImpl roleManager,
                                TenantRoleMapper roleMapper,
                                ParameterProcessor<Integer> parameterSetter) {
        this.roleManager = roleManager;
        this.roleMapper = roleMapper;
        this.parameterSetter = parameterSetter;
    }

    @GetMapping("roles")
    public QueryResult<TenantRoleDto> getList(HttpServletRequest request)
        throws Exception {
        Query<Integer> query =
            parameterSetter.buildquery(request.getParameterMap());

        QueryResult<TenantRole> result = roleManager.getList(query);

        QueryResult<TenantRoleDto> response = new QueryResult<>();
        response.setData(roleMapper.toDtoList(result.getData()));
        response.setMeta(result.getMeta());

        return response;
    }

    @GetMapping("role/{id}")
    public QueryResult<TenantRoleDto> find(@PathVariable Integer id,
                                           HttpServletRequest request) {
        Query<Integer> query =
            parameterSetter.buildquery(request.getParameterMap());
        query.setPrimaryId(id);

        QueryResult<TenantRole> result = roleManager.getSingle(query);

        QueryResult<TenantRoleDto> response = new QueryResult<>();
        response.setData(roleMapper.toDtoList(result.getData()));
        response.setMeta(result.getMeta());

        return response;
    }

    @PostMapping("roles")
    public ResponseEntity<List<TenantRoleDto>> saveAll(
        @RequestBody List<TenantRoleDto> roleDtoList) {
        List<TenantRole> roleList =
            roleMapper.dtoToList(roleDtoList);

        List<TenantRole> result = roleManager.createAll(roleList);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(roleMapper.toDtoList(result));
    }


    @PostMapping("role")
    public ResponseEntity<TenantRoleDto> save(
        @RequestBody TenantRoleDto roleDto)
        throws Exception {
        TenantRole role = roleMapper.dtoToObject(roleDto);

        role = roleManager.create(role);

        roleDto = roleMapper.toDto(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleDto);
    }

    @PutMapping("roles")
    public List<TenantRoleDto> updateAll(
        @RequestBody List<TenantRoleDto> roleDtoList) {
        List<TenantRole> roleList = roleMapper.dtoToList(roleDtoList);

        List<TenantRole> result = roleManager.updateAll(roleList);

        return roleMapper.toDtoList(result);
    }

    @PutMapping("role/{id}")
    public TenantRoleDto update(
        @RequestBody TenantRoleDto roleDto,
        @PathVariable Integer id)
        throws Exception {
        TenantRole role = roleMapper.dtoToObject(roleDto);

        role = roleManager.update(id, role);

        return roleMapper.toDto(role);
    }

    @PatchMapping("role/{id}")
    public TenantRoleDto updatePartial(
        @RequestBody TenantRoleDto roleDto,
        @PathVariable Integer id)
        throws Exception {
        TenantRole role = roleMapper.dtoToObject(roleDto);

        role = roleManager.updatePartial(id, role);

        return roleMapper.toDto(role);
    }

    @DeleteMapping("role/{id}")
    public void delete(@PathVariable Integer id) throws Exception {
        roleManager.delete(id);
    }
}