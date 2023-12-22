package template.tenants.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.global.utilities.ParameterProcessor;
import template.tenants.dtos.TenantUserDto;
import template.tenants.mappers.TenantUserMapper;
import template.tenants.models.TenantUser;
import template.tenants.services.TenantUserManager;

import java.util.List;

@RestController
@RequestMapping
public class TenantUserController {
    private final TenantUserManager userManager;
    private final TenantUserMapper userMapper;
    private final ParameterProcessor<Integer> parameterSetter;

    public TenantUserController(
        TenantUserManager userManager,
        TenantUserMapper userMapper,
        ParameterProcessor<Integer> parameterSetter) {
        this.userManager = userManager;
        this.userMapper = userMapper;
        this.parameterSetter = parameterSetter;
    }

    @DeleteMapping("user/{id}")
    public void delete(@PathVariable Integer id) throws Exception {
        userManager.delete(id);
    }

    @GetMapping("user/{id}")
    public QueryResult<TenantUserDto> find(@PathVariable Integer id,
                                           HttpServletRequest request) {
        Query<Integer> query =
            parameterSetter.buildQuery(request.getParameterMap());
        query.setPrimaryId(id);

        QueryResult<TenantUser> result = userManager.getSingle(query);

        QueryResult<TenantUserDto> response = new QueryResult<>();
        response.setData(userMapper.toDtoList(result.getData()));
        response.setMeta(result.getMeta());

        return response;
    }

    @GetMapping("users")
    public QueryResult<TenantUserDto> findList(
        HttpServletRequest request) {
        Query<Integer> query =
            parameterSetter.buildQuery(request.getParameterMap());

        QueryResult<TenantUser> result = userManager.getList(query);

        QueryResult<TenantUserDto> response = new QueryResult<>();
        response.setData(userMapper.toDtoList(result.getData()));
        response.setMeta(result.getMeta());

        return response;
    }

    @PostMapping("user")
    public ResponseEntity<TenantUserDto> save(
        @RequestBody TenantUserDto userDto) {
        TenantUser user =
            userMapper.dtoToObject(userDto);

        user = userManager.create(user);

        userDto = userMapper.toDto(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("users")
    public ResponseEntity<List<TenantUserDto>> saveAll(
        @RequestBody List<TenantUserDto> userDtoList) {
        List<TenantUser> userList = userMapper.dtoToList(userDtoList);

        List<TenantUser> result = userManager.createAll(userList);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userMapper.toDtoList(result));
    }

    @PutMapping("user/{id}")
    public ResponseEntity<TenantUserDto> update(
        @RequestBody TenantUserDto userDto,
        @PathVariable Integer id) {
        TenantUser user = userMapper.dtoToObject(userDto);

        user = userManager.update(id, user);

        userDto = userMapper.toDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PutMapping("users")
    public List<TenantUserDto> updateAll(
        @RequestBody List<TenantUserDto> userDtoList) {
        List<TenantUser> userList = userMapper.dtoToList(userDtoList);

        List<TenantUser> result = userManager.updateAll(userList);

        return userMapper.toDtoList(result);
    }

    @PatchMapping("user/{id}")
    public ResponseEntity<TenantUserDto> updatePartial(
        @RequestBody TenantUserDto userDto,
        @PathVariable Integer id) {
        TenantUser user = userMapper.dtoToObject(userDto);

        user = userManager.updatePartial(id, user);

        userDto = userMapper.toDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }
}
