package template.hypermedia.helpers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import template.database.helpers.DoubleTable;
import template.database.helpers.SingleTable;
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

    public TestController(ParameterProcessor<Integer> parameterSetter,
                          QueryBuilder<SingleTable, Integer> singleTableQueryBuilder,
                          QueryBuilder<DoubleTable, Integer> doubleTableQueryBuilder,
                          SingleTableMapper singleTableMapper,
                          DoubleTableMapper doubleTableMapper) {
        this.parameterSetter = parameterSetter;
        this.singleTableQueryBuilder = singleTableQueryBuilder;
        this.doubleTableQueryBuilder = doubleTableQueryBuilder;
        this.singleTableMapper = singleTableMapper;
        this.doubleTableMapper = doubleTableMapper;
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

    @GetMapping("endpoint-error")
    public void endpointError() {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Testing");
    }
}
