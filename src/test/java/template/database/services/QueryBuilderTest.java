package template.database.services;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import template.database.enums.QueryConjunctive;
import template.database.enums.QueryFilter;
import template.database.enums.QuerySort;
import template.database.helpers.DoubleTable;
import template.database.helpers.JoinTable;
import template.database.helpers.SingleTable;
import template.database.helpers.TripleTable;
import template.database.models.ApplicationDataSource;
import template.database.models.ColumnFilter;
import template.database.models.DatabaseConnectionContext;
import template.database.models.Query;
import template.helpers.TemplatePostgreSqlContainer;

import javax.sql.DataSource;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@Import(QueryBuilderTest.TestConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QueryBuilderTest {
    @ClassRule
    public static PostgreSQLContainer<TemplatePostgreSqlContainer>
        postgreSQLContainer =
        TemplatePostgreSqlContainer.getInstance();
    @Autowired
    QueryBuilder<SingleTable, Integer> queryBuilder;
    @Autowired
    QueryBuilder<DoubleTable, Integer> queryDoubleBuilder;
    @Autowired
    QueryBuilder<TripleTable, Integer> queryTripleBuilder;
    @Autowired
    QueryBuilder<JoinTable, Integer> queryJoinBuilder;

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenQuery_whenSingleTableEmpty_shouldReturnEmptyList() {
        Query<Integer> query = new Query<>();
        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(0, actual.size());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenStringFilterEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("stringColumn");
        filter.setValue("test_2");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());
        assertEquals("test_2", actual.get(0).getStringColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenStringFilterNotEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.NE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("stringColumn");
        filter.setValue("test_2");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals("test_1", actual.get(0).getStringColumn());
        assertEquals("test_3", actual.get(1).getStringColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenStringFilterLikeQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LIKE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("stringColumn");
        filter.setValue("t_2");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());
        assertEquals("test_2", actual.get(0).getStringColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenStringFilterGreaterThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("stringColumn");
        filter.setValue("test_1");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals("test_2", actual.get(0).getStringColumn());
        assertEquals("test_3", actual.get(1).getStringColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenStringFilterLessThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("stringColumn");
        filter.setValue("test_3");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals("test_1", actual.get(0).getStringColumn());
        assertEquals("test_2", actual.get(1).getStringColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenStringFilterGreaterThanOrEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("stringColumn");
        filter.setValue("test_2");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals("test_2", actual.get(0).getStringColumn());
        assertEquals("test_3", actual.get(1).getStringColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenStringFilterLessThanOrEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("stringColumn");
        filter.setValue("test_2");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals("test_1", actual.get(0).getStringColumn());
        assertEquals("test_2", actual.get(1).getStringColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenTextFilterEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("textColumn");
        filter.setValue("this is a test for text number 1");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());
        assertEquals("this is a test for text number 1",
            actual.get(0).getTextColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenTextFilterNotEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.NE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("textColumn");
        filter.setValue("this is a test for text number 1");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals("this is a test for text number 2",
            actual.get(0).getTextColumn());
        assertEquals("this is a test for text number 3",
            actual.get(1).getTextColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenTextFilterLikeQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LIKE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("textColumn");
        filter.setValue("number 1");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());
        assertEquals("this is a test for text number 1",
            actual.get(0).getTextColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenTextFilterGreaterThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("textColumn");
        filter.setValue("this is a test for text number 1");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals("this is a test for text number 2",
            actual.get(0).getTextColumn());
        assertEquals("this is a test for text number 3",
            actual.get(1).getTextColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenTextFilterLessThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("textColumn");
        filter.setValue("this is a test for text number 3");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals("this is a test for text number 1",
            actual.get(0).getTextColumn());
        assertEquals("this is a test for text number 2",
            actual.get(1).getTextColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenTextFilterGreaterThanOrEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("textColumn");
        filter.setValue("this is a test for text number 2");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals("this is a test for text number 2",
            actual.get(0).getTextColumn());
        assertEquals("this is a test for text number 3",
            actual.get(1).getTextColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenTextFilterLessThanOrEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("textColumn");
        filter.setValue("this is a test for text number 2");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals("this is a test for text number 1",
            actual.get(0).getTextColumn());
        assertEquals("this is a test for text number 2",
            actual.get(1).getTextColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenUUIDFilterEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("uuidColumn");
        filter.setValue("570c9397-1b91-4f46-9e91-8c8f030a01e0");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());
        assertEquals(UUID.fromString("570c9397-1b91-4f46-9e91-8c8f030a01e0"),
            actual.get(0).getUuidColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenUUIDFilterNotEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.NE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("uuidColumn");
        filter.setValue("570c9397-1b91-4f46-9e91-8c8f030a01e0");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenUUIDFilterLikeQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LIKE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("uuidColumn");
        filter.setValue("570c939");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenIntegerFilterEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("integerColumn");
        filter.setValue("3");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());
        assertEquals(Integer.valueOf(3), actual.get(0).getIntegerColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenIntegerFilterNotEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.NE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("integerColumn");
        filter.setValue("1");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals(Integer.valueOf(2), actual.get(0).getIntegerColumn());
        assertEquals(Integer.valueOf(3), actual.get(1).getIntegerColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenIntegerFilterLikeQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LIKE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("integerColumn");
        filter.setValue("3");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());
        assertEquals(Integer.valueOf(3), actual.get(0).getIntegerColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenIntegerFilterGreaterThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("integerColumn");
        filter.setValue("1");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals(Integer.valueOf(2), actual.get(0).getIntegerColumn());
        assertEquals(Integer.valueOf(3), actual.get(1).getIntegerColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenIntegerFilterLessThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("integerColumn");
        filter.setValue("3");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals(Integer.valueOf(1), actual.get(0).getIntegerColumn());
        assertEquals(Integer.valueOf(2), actual.get(1).getIntegerColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenIntegerFilterGreaterThanOrEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("integerColumn");
        filter.setValue("2");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals(Integer.valueOf(2), actual.get(0).getIntegerColumn());
        assertEquals(Integer.valueOf(3), actual.get(1).getIntegerColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenIntegerFilterLessThanOrEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("integerColumn");
        filter.setValue("2");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals(Integer.valueOf(1), actual.get(0).getIntegerColumn());
        assertEquals(Integer.valueOf(2), actual.get(1).getIntegerColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenFloatFilterEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("doubleColumn");
        filter.setValue("2.02");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());
        assertEquals(Double.valueOf(2.02), actual.get(0).getDoubleColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenFloatFilterNotEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.NE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("doubleColumn");
        filter.setValue("1.01");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals(Double.valueOf(2.02), actual.get(0).getDoubleColumn());
        assertEquals(Double.valueOf(3.03), actual.get(1).getDoubleColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenFloatFilterLikeQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LIKE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("doubleColumn");
        filter.setValue("2.02");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());
        assertEquals(Double.valueOf(2.02), actual.get(0).getDoubleColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenFloatFilterGreaterThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("doubleColumn");
        filter.setValue("1.01");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals(Double.valueOf(2.02), actual.get(0).getDoubleColumn());
        assertEquals(Double.valueOf(3.03), actual.get(1).getDoubleColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenFloatFilterLessThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("doubleColumn");
        filter.setValue("3.03");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals(Double.valueOf(1.01), actual.get(0).getDoubleColumn());
        assertEquals(Double.valueOf(2.02), actual.get(1).getDoubleColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenFloatFilterGreaterThanOrEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("doubleColumn");
        filter.setValue("2.02");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals(Double.valueOf(2.02), actual.get(0).getDoubleColumn());
        assertEquals(Double.valueOf(3.03), actual.get(1).getDoubleColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenFloatFilterLessThanOrEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("doubleColumn");
        filter.setValue("2.02");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        assertEquals(Double.valueOf(1.01), actual.get(0).getDoubleColumn());
        assertEquals(Double.valueOf(2.02), actual.get(1).getDoubleColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToYearFilterEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2021");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);


        assertEquals(1, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2021, zonedDateTime.getYear());
        assertEquals(1, zonedDateTime.getDayOfMonth());
        assertEquals(1, zonedDateTime.getMonthValue());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToYearFilterLikeQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LIKE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2021");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2021, zonedDateTime.getYear());
        assertEquals(1, zonedDateTime.getDayOfMonth());
        assertEquals(1, zonedDateTime.getMonthValue());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToYearFilterNotEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.NE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2021");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);


        assertEquals(2, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(3, zonedDateTime.getMonthValue());
        assertEquals(3, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToYearFilterGreaterThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2021");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(3, zonedDateTime.getMonthValue());
        assertEquals(3, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToYearFilterLessThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2023");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2021, zonedDateTime.getYear());
        assertEquals(1, zonedDateTime.getMonthValue());
        assertEquals(1, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToYearFilterGreaterThanOrEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2022");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);


        assertEquals(2, actual.size());
        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));

        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));

        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(3, zonedDateTime.getMonthValue());
        assertEquals(3, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToYearFilterLessThanOrEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2022");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);


        assertEquals(2, actual.size());
        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));

        assertEquals(2021, zonedDateTime.getYear());
        assertEquals(1, zonedDateTime.getMonthValue());
        assertEquals(1, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));

        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToMonthFilterEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2022-02");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToMonthFilterNotEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.NE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2022-02");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2021, zonedDateTime.getYear());
        assertEquals(1, zonedDateTime.getMonthValue());
        assertEquals(1, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(3, zonedDateTime.getMonthValue());
        assertEquals(3, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToMonthFilterLikeQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LIKE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2022-02");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToMonthFilterGreaterThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2021-01");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(3, zonedDateTime.getMonthValue());
        assertEquals(3, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToMonthFilterLessThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2023-03");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2021, zonedDateTime.getYear());
        assertEquals(1, zonedDateTime.getMonthValue());
        assertEquals(1, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToMonthFilterGreaterThanOrEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2022-02");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));

        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(3, zonedDateTime.getMonthValue());
        assertEquals(3, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToMonthFilterLessThanOrEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2022-02");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2021, zonedDateTime.getYear());
        assertEquals(1, zonedDateTime.getMonthValue());
        assertEquals(1, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToDayFilterEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2023-03-03");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(3, zonedDateTime.getMonthValue());
        assertEquals(3, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToDayFilterNotEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.NE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2023-03-03");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2021, zonedDateTime.getYear());
        assertEquals(1, zonedDateTime.getMonthValue());
        assertEquals(1, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToDayFilterLikeQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LIKE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2023-03-03");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(3, zonedDateTime.getMonthValue());
        assertEquals(3, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToDayFilterGreaterThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2021-01-01");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(3, zonedDateTime.getMonthValue());
        assertEquals(3, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToDayFilterLessThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2023-03-03");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2021, zonedDateTime.getYear());
        assertEquals(1, zonedDateTime.getMonthValue());
        assertEquals(1, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToDayFilterGreaterThanOrEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2022-02-02");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(3, zonedDateTime.getMonthValue());
        assertEquals(3, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTable.sql"})
    public void givenDateToDayFilterLessThanOrEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2022-02-02");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));

        assertEquals(2021, zonedDateTime.getYear());
        assertEquals(1, zonedDateTime.getMonthValue());
        assertEquals(1, zonedDateTime.getDayOfMonth());

        instant = actual.get(1).getDateColumn();
        zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2022, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTableTimes.sql"})
    public void givenDateToHourFilterEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2023-01-01T08");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(1, zonedDateTime.getMonthValue());
        assertEquals(1, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTableTimes.sql"})
    public void givenDateToMinuteFilterEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2023-01-01T08:15");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(1, zonedDateTime.getMonthValue());
        assertEquals(1, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTableTimes.sql"})
    public void givenDateToSecondsFilterEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2023-01-01T08:15:06");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(1, zonedDateTime.getMonthValue());
        assertEquals(1, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTableTimes.sql"})
    public void givenDateToMillisecondsFilterEqualQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2023-02-02T14:32:17.234");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(1, actual.size());

        Instant instant = actual.get(0).getDateColumn();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        assertEquals(2023, zonedDateTime.getYear());
        assertEquals(2, zonedDateTime.getMonthValue());
        assertEquals(2, zonedDateTime.getDayOfMonth());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/singleTableTimes.sql"})
    public void givenDateToMillisecondsFilterGreaterThanQuery_shouldReturnFilteredList() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.GT);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("dateColumn");
        filter.setValue("2023-03-03T20:53:38.591038");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<SingleTable> actual =
            queryBuilder.getList(SingleTable.class, query);

        assertEquals(2, actual.size());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/doubleTable.sql"})
    public void givenNestedEntityExists_whenSearchOnNested_shouldReturnNestedEntity() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("singleTable.stringColumn");
        filter.setValue("test_1");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<DoubleTable> actual =
            queryDoubleBuilder.getList(DoubleTable.class, query);

        assertEquals(1, actual.size());
        assertEquals("nested_test_1", actual.get(0).getStringColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/tripleTable.sql"})
    public void givenNestedNestedEntityExists_whenSearchOnNested_shouldReturnNestedEntity() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("doubleTable.singleTable.stringColumn");
        filter.setValue("test_1");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<TripleTable> actual =
            queryTripleBuilder.getList(TripleTable.class, query);

        assertEquals(1, actual.size());
        assertEquals("nested_nested_test_1", actual.get(0).getStringColumn());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/joinTable.sql"})
    public void givenJoinTableEntityExists_whenSearchOnJoinTable_shouldReturnJoinTableEntity() {
        Query<Integer> query = new Query<>();
        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.LTE);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("singleTables.stringColumn");
        filter.setValue("test_1");
        List<ColumnFilter> columnFilterList = new ArrayList<>();
        columnFilterList.add(filter);
        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<JoinTable> actual =
            queryJoinBuilder.getList(JoinTable.class, query);

        assertEquals(1, actual.size());
        assertEquals("join_test_1", actual.get(0).getName());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/joinTable.sql"})
    public void givenORFilter_whenSearchForMultipleFilter_shouldReturnAllEntities() {
        Query<Integer> query = new Query<>();
        List<ColumnFilter> columnFilterList = new ArrayList<>();

        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.OR);
        filter.setProperty("singleTables.stringColumn");
        filter.setValue("test_1");
        columnFilterList.add(filter);

        ColumnFilter filterTwo = new ColumnFilter();
        filterTwo.setFilter(QueryFilter.EQ);
        filterTwo.setConjunctive(QueryConjunctive.OR);
        filterTwo.setProperty("singleTables.stringColumn");
        filterTwo.setValue("test_2");
        columnFilterList.add(filterTwo);

        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<JoinTable> actual =
            queryJoinBuilder.getList(JoinTable.class, query);

        assertEquals(2, actual.size());
        assertEquals("join_test_1", actual.get(0).getName());
        assertEquals("join_test_2", actual.get(1).getName());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/joinTable.sql"})
    public void givenSortDesc_shouldDescOrderedList() {
        Query<Integer> query = new Query<>();
        List<ColumnFilter> columnFilterList = new ArrayList<>();

        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.OR);
        filter.setProperty("singleTables.stringColumn");
        filter.setValue("test_1");
        columnFilterList.add(filter);

        ColumnFilter filterTwo = new ColumnFilter();
        filterTwo.setFilter(QueryFilter.EQ);
        filterTwo.setConjunctive(QueryConjunctive.OR);
        filterTwo.setProperty("singleTables.stringColumn");
        filterTwo.setValue("test_2");
        columnFilterList.add(filterTwo);

        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);
        HashMap<QuerySort, String[]> sortList = new HashMap<>();
        String[] list = {"name"};
        sortList.put(QuerySort.DESC, list);
        query.setSortList(sortList);
        List<JoinTable> actual =
            queryJoinBuilder.getList(JoinTable.class, query);

        assertEquals(2, actual.size());
        assertEquals("join_test_1", actual.get(1).getName());
        assertEquals("join_test_2", actual.get(0).getName());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql",
        "classpath:sql/database/joinTable.sql"})
    public void givenANDFilter_whenSearchForMultipleFilter_shouldReturnNoEntities() {
        Query<Integer> query = new Query<>();
        List<ColumnFilter> columnFilterList = new ArrayList<>();

        ColumnFilter filter = new ColumnFilter();
        filter.setFilter(QueryFilter.EQ);
        filter.setConjunctive(QueryConjunctive.AND);
        filter.setProperty("singleTables.stringColumn");
        filter.setValue("test_1");
        columnFilterList.add(filter);

        ColumnFilter filterTwo = new ColumnFilter();
        filterTwo.setFilter(QueryFilter.EQ);
        filterTwo.setConjunctive(QueryConjunctive.AND);
        filterTwo.setProperty("singleTables.stringColumn");
        filterTwo.setValue("test_2");
        columnFilterList.add(filterTwo);

        List<List<ColumnFilter>> filterList = new ArrayList<>();
        filterList.add(columnFilterList);
        query.setFilterList(filterList);

        List<JoinTable> actual =
            queryJoinBuilder.getList(JoinTable.class, query);

        assertEquals(0, actual.size());
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        @Primary
        public DataSource dataSource() {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName(
                postgreSQLContainer.getDriverClassName());
            dataSource.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
            dataSource.setUsername(postgreSQLContainer.getUsername());
            dataSource.setPassword(postgreSQLContainer.getPassword());
            dataSource.setMinimumIdle(1);
            dataSource.setMaximumPoolSize(2);

            JdbcTemplate testDatabase = new JdbcTemplate(dataSource);
            testDatabase.execute("CREATE DATABASE query_builder;");

            dataSource = new HikariDataSource();
            dataSource.setDriverClassName(
                postgreSQLContainer.getDriverClassName());
            dataSource.setJdbcUrl(postgreSQLContainer.getJdbcUrl()
                .replace("test", "query_builder"));
            dataSource.setUsername(postgreSQLContainer.getUsername());
            dataSource.setPassword(postgreSQLContainer.getPassword());
            dataSource.setMinimumIdle(1);
            dataSource.setMaximumPoolSize(2);

            ApplicationDataSource applicationDataSource =
                new ApplicationDataSource();
            applicationDataSource.setDataSource(dataSource);
            DatabaseConnectionContext.setCurrentDatabaseConnection(
                applicationDataSource);

            return dataSource;
        }
    }
}
