package template.hypermedia.helpers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import template.database.helpers.SingleTable;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.database.services.QueryBuilder;
import template.global.services.QueryProvider;
import template.global.utilities.ParameterProcessor;

import java.util.List;

@RestController
@RequestMapping()
public class TestController {
    private final ParameterProcessor<Integer> parameterSetter;
    private final QueryProvider<SingleTable, Integer, SingleTableDto>
        queryProvider;
    private final QueryBuilder<SingleTable, Integer> singleTableQueryBuilder;
    private final SingleTableMapper singleTableMapper;

    public TestController(ParameterProcessor<Integer> parameterSetter,
                          QueryProvider<SingleTable, Integer, SingleTableDto> queryProvider,
                          QueryBuilder<SingleTable, Integer> singleTableQueryBuilder,
                          SingleTableMapper singleTableMapper) {
        this.parameterSetter = parameterSetter;
        this.queryProvider = queryProvider;
        this.singleTableQueryBuilder = singleTableQueryBuilder;
        this.singleTableMapper = singleTableMapper;
    }

    @GetMapping("tests")
    public void placeholder() {
    }

    @GetMapping("single-tables")
    public QueryResult<SingleTableDto> findAllSingleTables(
        HttpServletRequest request) {
        Query<Integer> query =
            parameterSetter.buildquery(request.getParameterMap());

        List<SingleTable> result =
            singleTableQueryBuilder.getList(SingleTable.class, query);

        return queryProvider.buildQueryResult(SingleTable.class, query,
            result, singleTableMapper.toSingleTableDtoList(result));
    }

    @GetMapping("endpoint-error")
    public void endpointError() {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Testing");
    }
}
