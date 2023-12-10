package template.internal.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.global.utilities.ParameterProcessor;
import template.internal.dtos.InternalUserDto;
import template.internal.mappers.InternalUserMapper;
import template.internal.models.InternalUser;
import template.internal.services.InternalUserManager;

import java.util.List;

@RestController
@RequestMapping("internal")
public class InternalUserController {
    private final InternalUserManager userManager;
    private final InternalUserMapper userMapper;
    private final ParameterProcessor<Integer> parameterSetter;

    public InternalUserController(
        InternalUserManager userManager,
        InternalUserMapper userMapper,
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
    public QueryResult<InternalUserDto> find(@PathVariable Integer id,
                                             HttpServletRequest request) {
        Query<Integer> query =
            parameterSetter.buildquery(request.getParameterMap());
        query.setPrimaryId(id);

        QueryResult<InternalUser> result =
            userManager.getSingle(query);

        QueryResult<InternalUserDto> response = new QueryResult<>();
        response.setData(userMapper.toDtoList(result.getData()));
        response.setMeta(result.getMeta());

        return response;
    }

    @GetMapping("users")
    public QueryResult<InternalUserDto> findList(
        HttpServletRequest request) {
        Query<Integer> query =
            parameterSetter.buildquery(request.getParameterMap());

        QueryResult<InternalUser> result =
            userManager.getList(query);

        QueryResult<InternalUserDto> response = new QueryResult<>();
        response.setData(userMapper.toDtoList(result.getData()));
        response.setMeta(result.getMeta());

        return response;
    }

    @PostMapping("user")
    public ResponseEntity<InternalUserDto> save(
        @RequestBody InternalUserDto userDto)
        throws Exception {
        InternalUser user =
            userMapper.dtoToObject(userDto);

        user = userManager.create(user);

        userDto = userMapper.toDto(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("users")
    public ResponseEntity<List<InternalUserDto>> saveAll(
        @RequestBody List<InternalUserDto> userDtoList) {
        List<InternalUser> userList =
            userMapper.dtoToList(userDtoList);

        List<InternalUser> result =
            userManager.createAll(userList);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userMapper.toDtoList(result));
    }

    @PutMapping("user/{id}")
    public ResponseEntity<InternalUserDto> update(
        @RequestBody InternalUserDto userDto,
        @PathVariable Integer id)
        throws Exception {
        InternalUser user =
            userMapper.dtoToObject(userDto);

        user = userManager.update(id, user);

        userDto = userMapper.toDto(
            user);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PutMapping("users")
    public List<InternalUserDto> updateAll(
        @RequestBody List<InternalUserDto> userDtoList) {
        List<InternalUser> userList =
            userMapper.dtoToList(userDtoList);

        List<InternalUser> result =
            userManager.updateAll(userList);

        return userMapper.toDtoList(result);
    }

    @PatchMapping("user/{id}")
    public ResponseEntity<InternalUserDto> updatePartial(
        @RequestBody InternalUserDto userDto,
        @PathVariable Integer id)
        throws Exception {
        InternalUser user =
            userMapper.dtoToObject(userDto);

        user = userManager.updatePartial(id, user);

        userDto = userMapper.toDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }
}
