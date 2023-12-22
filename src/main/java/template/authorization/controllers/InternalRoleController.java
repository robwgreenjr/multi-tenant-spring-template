package template.authorization.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import template.authorization.dtos.InternalRoleDto;
import template.authorization.mappers.InternalRoleMapper;
import template.authorization.models.InternalRole;
import template.authorization.services.InternalRoleManagerImpl;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.global.utilities.ParameterProcessor;

import java.util.List;

@RestController
@RequestMapping("internal/authorization")
public class InternalRoleController {
    private final InternalRoleManagerImpl roleManager;
    private final InternalRoleMapper roleMapper;
    private final ParameterProcessor<Integer> parameterSetter;

    public InternalRoleController(InternalRoleManagerImpl roleManager,
                                  InternalRoleMapper roleMapper,
                                  ParameterProcessor<Integer> parameterSetter) {
        this.roleManager = roleManager;
        this.roleMapper = roleMapper;
        this.parameterSetter = parameterSetter;
    }

    @GetMapping("roles")
    public QueryResult<InternalRoleDto> getList(HttpServletRequest request)
        throws Exception {
        Query<Integer> query =
            parameterSetter.buildQuery(request.getParameterMap());

        QueryResult<InternalRole> result = roleManager.getList(query);

        QueryResult<InternalRoleDto> response = new QueryResult<>();
        response.setData(roleMapper.toDtoList(result.getData()));
        response.setMeta(result.getMeta());

        return response;
    }

    @GetMapping("role/{id}")
    public QueryResult<InternalRoleDto> find(@PathVariable Integer id,
                                             HttpServletRequest request) {
        Query<Integer> query =
            parameterSetter.buildQuery(request.getParameterMap());
        query.setPrimaryId(id);

        QueryResult<InternalRole> result = roleManager.getSingle(query);

        QueryResult<InternalRoleDto> response = new QueryResult<>();
        response.setData(roleMapper.toDtoList(result.getData()));
        response.setMeta(result.getMeta());

        return response;
    }

    @PostMapping("roles")
    public ResponseEntity<List<InternalRoleDto>> saveAll(
        @RequestBody List<InternalRoleDto> roleDtoList) {
        List<InternalRole> roleList =
            roleMapper.dtoToList(roleDtoList);

        List<InternalRole> result = roleManager.createAll(roleList);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(roleMapper.toDtoList(result));
    }


    @PostMapping("role")
    public ResponseEntity<InternalRoleDto> save(
        @RequestBody InternalRoleDto roleDto)
        throws Exception {
        InternalRole role = roleMapper.dtoToObject(roleDto);

        role = roleManager.create(role);

        roleDto = roleMapper.toDto(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleDto);
    }

    @PutMapping("roles")
    public List<InternalRoleDto> updateAll(
        @RequestBody List<InternalRoleDto> roleDtoList) {
        List<InternalRole> roleList = roleMapper.dtoToList(roleDtoList);

        List<InternalRole> result = roleManager.updateAll(roleList);

        return roleMapper.toDtoList(result);
    }

    @PutMapping("role/{id}")
    public InternalRoleDto update(
        @RequestBody InternalRoleDto roleDto,
        @PathVariable Integer id)
        throws Exception {
        InternalRole role = roleMapper.dtoToObject(roleDto);

        role = roleManager.update(id, role);

        return roleMapper.toDto(role);
    }

    @PatchMapping("role/{id}")
    public InternalRoleDto updatePartial(
        @RequestBody InternalRoleDto roleDto,
        @PathVariable Integer id)
        throws Exception {
        InternalRole role = roleMapper.dtoToObject(roleDto);

        role = roleManager.updatePartial(id, role);

        return roleMapper.toDto(role);
    }

    @DeleteMapping("role/{id}")
    public void delete(@PathVariable Integer id) throws Exception {
        roleManager.delete(id);
    }
}