package template.global.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import template.database.enums.QueryConjunctive;
import template.database.enums.QueryFilter;
import template.database.enums.QuerySort;
import template.database.models.ColumnFilterList;
import template.database.models.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterSetterTest {
    private final ParameterProcessor parameterSetter =
        new ParameterProcessorImpl();

    @Test
    public void givenAscSort_whenCallingBuildquery_shouldReturnqueryWithAsc() {
        Map<String, String[]> queryParameters = new HashMap<>();
        String[] list = {"asc(name)"};
        queryParameters.put("sort_by", list);

        Query actual = parameterSetter.buildQuery(queryParameters);
        HashMap<QuerySort, String[]> sortList = actual.getSortList();

        Assertions.assertNotNull(sortList);
        Assertions.assertArrayEquals(new String[]{"name"},
            sortList.get(QuerySort.ASC));
    }

    @Test
    public void givenAscAndDescSort_whenCallingBuildquery_shouldReturnqueryWithAscAndDesc() {
        Map<String, String[]> queryParameters = new HashMap<>();
        String[] list = {"asc(name),desc(age)"};
        queryParameters.put("sort_by", list);

        Query actual = parameterSetter.buildQuery(queryParameters);
        HashMap<QuerySort, String[]> sortList = actual.getSortList();

        Assertions.assertNotNull(sortList);
        Assertions.assertArrayEquals(sortList.get(QuerySort.ASC),
            new String[]{"name"});
        Assertions.assertArrayEquals(sortList.get(QuerySort.DESC),
            new String[]{"age"});
    }

    @Test
    public void givenMultipleAsc_whenCallingBuildquery_shouldReturnqueryWithAllAsc() {
        Map<String, String[]> queryParameters = new HashMap<>();
        String[] list = {"asc(name,age,birthday)"};
        queryParameters.put("sort_by", list);

        Query actual = parameterSetter.buildQuery(queryParameters);
        HashMap<QuerySort, String[]> sortList = actual.getSortList();

        Assertions.assertNotNull(sortList);
        Assertions.assertArrayEquals(new String[]{"name", "age", "birthday"},
            sortList.get(QuerySort.ASC));
    }

    @Test
    public void givenMultipleDesc_whenCallingBuildquery_shouldReturnqueryWithAllDesc() {
        Map<String, String[]> queryParameters = new HashMap<>();
        String[] list = {"desc(name,age,birthday)"};
        queryParameters.put("sort_by", list);

        Query actual = parameterSetter.buildQuery(queryParameters);
        HashMap<QuerySort, String[]> sortList = actual.getSortList();

        Assertions.assertNotNull(sortList);
        Assertions.assertArrayEquals(new String[]{"name", "age", "birthday"},
            sortList.get(QuerySort.DESC));
    }

    @Test
    public void givenLimit_whenCallingBuildquery_shouldReturnqueryWithLimit() {
        Map<String, String[]> queryParameters = new HashMap<>();
        String[] list = {"100"};
        queryParameters.put("limit", list);

        Query actual = parameterSetter.buildQuery(queryParameters);
        Integer limit = actual.getLimit();

        Assertions.assertNotNull(limit);
        Assertions.assertEquals(100, (int) limit);
    }

    @Test
    public void givenOneFilterParameter_whenCallingBuildquery_shouldReturnqueryFilter() {
        Map<String, String[]> queryParameters = new HashMap<>();
        String[] list = {"testing@gmail.com"};
        queryParameters.put("email", list);

        Query actual = parameterSetter.buildQuery(queryParameters);
        List<ColumnFilterList> filterList = actual.getFilterList();

        Assertions.assertNotNull(filterList);
        Assertions.assertEquals(QueryConjunctive.AND,
            filterList.get(0).getFilters().get(0).getConjunctive());
        Assertions.assertEquals(QueryFilter.EQ,
            filterList.get(0).getFilters().get(0).getFilter());
        Assertions.assertEquals("testing@gmail.com",
            filterList.get(0).getFilters().get(0).getValue());
        Assertions.assertEquals("email",
            filterList.get(0).getFilters().get(0).getProperty());
    }

    @Test
    public void givenTwoFilterParameters_whenCallingBuildquery_shouldReturnqueryFilters() {
        Map<String, String[]> queryParameters = new HashMap<>();
        String[] list = {"testing@gmail.com"};
        queryParameters.put("email", list);
        String[] listTwo = {"Tester"};
        queryParameters.put("firstName", listTwo);

        Query actual = parameterSetter.buildQuery(queryParameters);
        List<ColumnFilterList> filterList = actual.getFilterList();

        Assertions.assertNotNull(filterList);
        Assertions.assertEquals(QueryConjunctive.AND,
            filterList.get(0).getFilters().get(0).getConjunctive());
        Assertions.assertEquals(QueryFilter.EQ,
            filterList.get(0).getFilters().get(0).getFilter());
        Assertions.assertEquals("Tester",
            filterList.get(0).getFilters().get(0).getValue());
        Assertions.assertEquals("firstName",
            filterList.get(0).getFilters().get(0).getProperty());

        Assertions.assertEquals(QueryConjunctive.AND,
            filterList.get(1).getFilters().get(0).getConjunctive());
        Assertions.assertEquals(QueryFilter.EQ,
            filterList.get(1).getFilters().get(0).getFilter());
        Assertions.assertEquals("testing@gmail.com",
            filterList.get(1).getFilters().get(0).getValue());
        Assertions.assertEquals("email",
            filterList.get(1).getFilters().get(0).getProperty());
    }

    @Test
    public void givenTwoFilterParametersWithOr_whenCallingBuildquery_shouldReturnqueryFiltersWithOr() {
        Map<String, String[]> queryParameters = new HashMap<>();
        String[] list = {"testing@gmail.com[or]email=another@gmail.com"};
        queryParameters.put("email", list);

        Query actual = parameterSetter.buildQuery(queryParameters);
        List<ColumnFilterList> filterList = actual.getFilterList();

        Assertions.assertNotNull(filterList);
        Assertions.assertEquals(QueryConjunctive.AND,
            filterList.get(0).getFilters().get(0).getConjunctive());
        Assertions.assertEquals(QueryFilter.EQ,
            filterList.get(0).getFilters().get(0).getFilter());
        Assertions.assertEquals("testing@gmail.com",
            filterList.get(0).getFilters().get(0).getValue());
        Assertions.assertEquals("email",
            filterList.get(0).getFilters().get(0).getProperty());

        Assertions.assertEquals(QueryConjunctive.OR,
            filterList.get(0).getFilters().get(1).getConjunctive());
        Assertions.assertEquals(QueryFilter.EQ,
            filterList.get(0).getFilters().get(1).getFilter());
        Assertions.assertEquals("another@gmail.com",
            filterList.get(0).getFilters().get(1).getValue());
        Assertions.assertEquals("email",
            filterList.get(0).getFilters().get(1).getProperty());
    }

    @Test
    public void givenFilterParametersWithLessThan_whenCallingBuildquery_shouldReturnqueryFilterWithLessThan() {
        Map<String, String[]> queryParameters = new HashMap<>();
        String[] list = {"testing@gmail.com"};
        queryParameters.put("email[lte]", list);

        Query actual = parameterSetter.buildQuery(queryParameters);
        List<ColumnFilterList> filterList = actual.getFilterList();

        Assertions.assertNotNull(filterList);
        Assertions.assertEquals(QueryConjunctive.AND,
            filterList.get(0).getFilters().get(0).getConjunctive());
        Assertions.assertEquals(QueryFilter.LTE,
            filterList.get(0).getFilters().get(0).getFilter());
        Assertions.assertEquals("testing@gmail.com",
            filterList.get(0).getFilters().get(0).getValue());
        Assertions.assertEquals("email",
            filterList.get(0).getFilters().get(0).getProperty());
    }

    @Test
    public void givenFilterParametersWithGreaterThan_whenCallingBuildquery_shouldReturnqueryFilterWithGreaterThan() {
        Map<String, String[]> queryParameters = new HashMap<>();
        String[] list = {"testing@gmail.com"};
        queryParameters.put("email[gte]", list);

        Query actual = parameterSetter.buildQuery(queryParameters);
        List<ColumnFilterList> filterList = actual.getFilterList();

        Assertions.assertNotNull(filterList);
        Assertions.assertEquals(QueryConjunctive.AND,
            filterList.get(0).getFilters().get(0).getConjunctive());
        Assertions.assertEquals(QueryFilter.GTE,
            filterList.get(0).getFilters().get(0).getFilter());
        Assertions.assertEquals("testing@gmail.com",
            filterList.get(0).getFilters().get(0).getValue());
        Assertions.assertEquals("email",
            filterList.get(0).getFilters().get(0).getProperty());
    }

    @Test
    public void givenThreeFiltersParametersWithALot_whenCallingBuildquery_shouldReturnqueryFilterWithALot() {
        Map<String, String[]> queryParameters = new HashMap<>();
        String[] list = {"testing@gmail.com[or]age[lte]=25"};
        queryParameters.put("email[gte]", list);

        String[] listTwo = {"Tester"};
        queryParameters.put("firstName", listTwo);

        Query actual = parameterSetter.buildQuery(queryParameters);
        List<ColumnFilterList> filterList = actual.getFilterList();

        Assertions.assertNotNull(filterList);
        Assertions.assertEquals(QueryConjunctive.AND,
            filterList.get(0).getFilters().get(0).getConjunctive());
        Assertions.assertEquals(QueryFilter.EQ,
            filterList.get(0).getFilters().get(0).getFilter());
        Assertions.assertEquals("Tester",
            filterList.get(0).getFilters().get(0).getValue());
        Assertions.assertEquals("firstName",
            filterList.get(0).getFilters().get(0).getProperty());

        Assertions.assertEquals(QueryConjunctive.OR,
            filterList.get(1).getFilters().get(1).getConjunctive());
        Assertions.assertEquals(QueryFilter.LTE,
            filterList.get(1).getFilters().get(1).getFilter());
        Assertions.assertEquals("25",
            filterList.get(1).getFilters().get(1).getValue());
        Assertions.assertEquals("age",
            filterList.get(1).getFilters().get(1).getProperty());

        Assertions.assertEquals(QueryConjunctive.AND,
            filterList.get(1).getFilters().get(0).getConjunctive());
        Assertions.assertEquals(QueryFilter.GTE,
            filterList.get(1).getFilters().get(0).getFilter());
        Assertions.assertEquals("testing@gmail.com",
            filterList.get(1).getFilters().get(0).getValue());
        Assertions.assertEquals("email",
            filterList.get(1).getFilters().get(0).getProperty());
    }

    @Test
    public void givenSixFiltersParametersInOne_whenCallingBuildquery_shouldReturnqueryFilterWithSix() {
        Map<String, String[]> queryParameters = new HashMap<>();
        String[] list =
            {"testing@gmail.com[or]age[lte]=25[and]firstName[gte]=tester[and]lastName=blue[or]birthday[lte]=04-21-2010[and]createdOn=01-04-2022"};
        queryParameters.put("email[gte]", list);

        Query actual = parameterSetter.buildQuery(queryParameters);
        List<ColumnFilterList> filterList = actual.getFilterList();

        Assertions.assertNotNull(filterList);
        Assertions.assertEquals(QueryConjunctive.AND,
            filterList.get(0).getFilters().get(0).getConjunctive());
        Assertions.assertEquals(QueryFilter.GTE,
            filterList.get(0).getFilters().get(0).getFilter());
        Assertions.assertEquals("testing@gmail.com",
            filterList.get(0).getFilters().get(0).getValue());
        Assertions.assertEquals("email",
            filterList.get(0).getFilters().get(0).getProperty());

        Assertions.assertEquals(QueryConjunctive.OR,
            filterList.get(0).getFilters().get(1).getConjunctive());
        Assertions.assertEquals(QueryFilter.LTE,
            filterList.get(0).getFilters().get(1).getFilter());
        Assertions.assertEquals("25",
            filterList.get(0).getFilters().get(1).getValue());
        Assertions.assertEquals("age",
            filterList.get(0).getFilters().get(1).getProperty());

        Assertions.assertEquals(QueryConjunctive.AND,
            filterList.get(0).getFilters().get(2).getConjunctive());
        Assertions.assertEquals(QueryFilter.GTE,
            filterList.get(0).getFilters().get(2).getFilter());
        Assertions.assertEquals("tester",
            filterList.get(0).getFilters().get(2).getValue());
        Assertions.assertEquals("firstName",
            filterList.get(0).getFilters().get(2).getProperty());

        Assertions.assertEquals(QueryConjunctive.AND,
            filterList.get(0).getFilters().get(3).getConjunctive());
        Assertions.assertEquals(QueryFilter.EQ,
            filterList.get(0).getFilters().get(3).getFilter());
        Assertions.assertEquals("blue",
            filterList.get(0).getFilters().get(3).getValue());
        Assertions.assertEquals("lastName",
            filterList.get(0).getFilters().get(3).getProperty());

        Assertions.assertEquals(QueryConjunctive.OR,
            filterList.get(0).getFilters().get(4).getConjunctive());
        Assertions.assertEquals(QueryFilter.LTE,
            filterList.get(0).getFilters().get(4).getFilter());
        Assertions.assertEquals("04-21-2010",
            filterList.get(0).getFilters().get(4).getValue());
        Assertions.assertEquals("birthday",
            filterList.get(0).getFilters().get(4).getProperty());

        Assertions.assertEquals(QueryConjunctive.AND,
            filterList.get(0).getFilters().get(5).getConjunctive());
        Assertions.assertEquals(QueryFilter.EQ,
            filterList.get(0).getFilters().get(5).getFilter());
        Assertions.assertEquals("01-04-2022",
            filterList.get(0).getFilters().get(5).getValue());
        Assertions.assertEquals("createdOn",
            filterList.get(0).getFilters().get(5).getProperty());
    }
}