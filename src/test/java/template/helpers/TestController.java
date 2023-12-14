package template.helpers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.database.services.QueryBuilder;
import template.global.utilities.ParameterProcessor;

@RestController
@RequestMapping()
public class TestController {
    private final ParameterProcessor<Integer> parameterSetter;
    private final QueryBuilder<SingleTable, Integer> singleTableQueryBuilder;
    private final QueryBuilder<DoubleTable, Integer> doubleTableQueryBuilder;
    private final SingleTableMapper singleTableMapper;
    private final DoubleTableMapper doubleTableMapper;
    private final TripleTableMapper tripleTableMapper;
    private final QueryBuilder<TripleTable, Integer> tripleTableQueryBuilder;

    public TestController(ParameterProcessor<Integer> parameterSetter,
                          QueryBuilder<SingleTable, Integer> singleTableQueryBuilder,
                          QueryBuilder<DoubleTable, Integer> doubleTableQueryBuilder,
                          SingleTableMapper singleTableMapper,
                          DoubleTableMapper doubleTableMapper,
                          TripleTableMapper tripleTableMapper,
                          QueryBuilder<TripleTable, Integer> tripleTableQueryBuilder) {
        this.parameterSetter = parameterSetter;
        this.singleTableQueryBuilder = singleTableQueryBuilder;
        this.doubleTableQueryBuilder = doubleTableQueryBuilder;
        this.singleTableMapper = singleTableMapper;
        this.doubleTableMapper = doubleTableMapper;
        this.tripleTableMapper = tripleTableMapper;
        this.tripleTableQueryBuilder = tripleTableQueryBuilder;
    }

    @GetMapping("tests")
    public void placeholder() {
    }

    @GetMapping("single-tables")
    public QueryResult<SingleTableDto> findAllSingleTables(
        HttpServletRequest request) {
        Query<Integer> query =
            parameterSetter.buildquery(request.getParameterMap());

        QueryResult<SingleTable> result =
            singleTableQueryBuilder.getList(SingleTable.class, query);

        return result.mapData(
            singleTableMapper.toSingleTableDtoList(result.getData()));
    }

    @GetMapping("double-tables")
    public QueryResult<DoubleTableDto> findAllDoubleTables(
        HttpServletRequest request) {
        Query<Integer> query =
            parameterSetter.buildquery(request.getParameterMap());

        QueryResult<DoubleTable> result =
            doubleTableQueryBuilder.getList(DoubleTable.class, query);

        return result.mapData(
            doubleTableMapper.toDoubleTableDtoList(result.getData()));
    }

    @GetMapping("triple-tables")
    public QueryResult<TripleTableDto> findAllTripleTables(
        HttpServletRequest request) {
        Query<Integer> query =
            parameterSetter.buildquery(request.getParameterMap());

        QueryResult<TripleTable> result =
            tripleTableQueryBuilder.getList(TripleTable.class, query);

        return result.mapData(
            tripleTableMapper.toTripleTableDtoList(result.getData()));
    }

    @GetMapping("endpoint-error")
    public void endpointError() {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Testing");
    }
}
