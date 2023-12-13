package template.database.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.apache.commons.validator.GenericValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import template.database.enums.QueryConjunctive;
import template.database.enums.QuerySort;
import template.database.exceptions.InvalidDateFormatException;
import template.database.models.ColumnFilter;
import template.database.models.ColumnFilterList;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.global.utilities.StringParser;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class QueryBuilderImpl<T, S> implements QueryBuilder<T, S> {
    private final StringParser stringParser;
    @PersistenceContext
    private EntityManager entityManager;

    public QueryBuilderImpl(StringParser stringParser) {
        this.stringParser = stringParser;
    }

    private QueryResult<T> buildQueryResult(Class<T> entity,
                                            Query<S> query,
                                            List<T> data) {
        QueryResult<T> result = new QueryResult<>();
        result.setData(data);
        result.getMeta().setLimit(query.getLimit());
        result.getMeta().setCount(getCount(entity, query));
        result.getMeta().setCursor("id");
        result.getMeta().setPage(
            getCurrentPage(entity, query, result.getMeta().getCount()));
        result.getMeta().setNext(getNextCursor(entity, query));
        result.getMeta().setPrevious(getPreviousCursor(entity, query, data));
        result.getMeta().setLimit(query.getLimit());

        return result;
    }

    @Override
    public T getSingle(Class<T> entity, Query<S> query) {
        CriteriaQuery<Object> select = buildQuery(entity, query);

        return (T) entityManager.createQuery(select).getSingleResult();
    }

    @Override
    public QueryResult<T> getList(Class<T> entity, Query<S> query) {
        List<T> entityList = new ArrayList<>();

        CriteriaQuery<Object> select = buildQuery(entity, query);

        List<Object> resultList =
            entityManager.createQuery(select)
                .setMaxResults(query.getLimit())
                .getResultList();

        for (Object result : resultList) {
            entityList.add((T) result);
        }

        return buildQueryResult(entity, query, entityList);
    }

    @Override
    public Integer getCount(Class<T> entity, Query<S> query) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery =
            criteriaBuilder.createQuery(Long.class);

        Root<T> table = criteriaQuery.from(entity);
        CriteriaQuery<Long> select =
            criteriaQuery.select(criteriaBuilder.count(table));

        Query<S> countQuery = new Query<>(query);
        countQuery.removeCursor();

        buildCriteriaBuilderSelect(criteriaBuilder, select, countQuery, table);

        return Math.toIntExact(
            entityManager.createQuery(select).getSingleResult());
    }

    @Override
    public Integer getCurrentPage(Class<T> entity, Query<S> query,
                                  Integer count) {
        Query<S> currentPagequery = new Query<>(query);

        // If a cursor wasn't used then we know we are on first page
        if (!currentPagequery.isCursorSet()) {
            return 1;
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery =
            criteriaBuilder.createQuery(Long.class);

        Root<T> table = criteriaQuery.from(entity);
        CriteriaQuery<Long> select =
            criteriaQuery.select(criteriaBuilder.count(table));

        buildCriteriaBuilderSelect(criteriaBuilder, select,
            currentPagequery, table);

        Integer remainingCount = Math.toIntExact(
            entityManager.createQuery(select).getSingleResult());

        return (int) Math.ceil(
            (double) (count - remainingCount) /
                currentPagequery.getLimit()) + 1;
    }

    @Override
    public Integer getNextCursor(Class<T> entity, Query<S> query) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();

        Root<T> table = criteriaQuery.from(entity);
        CriteriaQuery<Object> select = criteriaQuery.select(table);

        Query<S> nextquery = new Query<>(query);
        nextquery.setLimit(query.getLimit() + 1);

        buildSortCriteria(criteriaBuilder, criteriaQuery, nextquery,
            table);
        buildCriteriaBuilderSelect(criteriaBuilder, select, nextquery,
            table);

        T nextObject = null;
        try {
            List<T> nextObjects = (List<T>) entityManager.createQuery(select)
                .setMaxResults(nextquery.getLimit())
                .getResultList();

            // If there is no extra object then we don't need a next cursor
            if (nextObjects.size() != nextquery.getLimit()) {
                return null;
            }

            nextObject = nextObjects.get(nextObjects.size() - 1);
        } catch (Exception exception) {
            // do nothing
        }

        if (nextObject == null) {
            return null;
        }

        int nextCursor = 0;
        try {
            Field fieldNameField =
                nextObject.getClass().getDeclaredField("id");
            fieldNameField.setAccessible(true);
            Object fieldValue = fieldNameField.get(nextObject);

            nextCursor = (Integer) fieldValue;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // do nothing
        }

        if (nextCursor == 0) {
            return null;
        }

        return nextCursor;
    }

    @Override
    public Integer getPreviousCursor(Class<T> entity,
                                     Query<S> query, List<T> data) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();

        Root<T> table = criteriaQuery.from(entity);
        CriteriaQuery<Object> select = criteriaQuery.select(table);

        HashMap<QuerySort, String[]> sortHashMap = new HashMap<>();
        sortHashMap.put(QuerySort.DESC, new String[]{"id"});
        Query<S> previousquery =
            new Query<>(query, sortHashMap);
        previousquery.removeIdCursor();

        Integer findPreviousCursor = 0;
        try {
            Field fieldNameField =
                data.get(0).getClass().getDeclaredField("id");
            fieldNameField.setAccessible(true);
            Object fieldValue = fieldNameField.get(data.get(0));

            findPreviousCursor = (Integer) fieldValue;
        } catch (Exception e) {
            // do nothing
        }

        previousquery.setLessThan("id", (S) findPreviousCursor);

        buildSortCriteria(criteriaBuilder, criteriaQuery, previousquery,
            table);
        buildCriteriaBuilderSelect(criteriaBuilder, select, previousquery,
            table);

        T previousObject = null;
        try {
            List<T> previousObjects =
                (List<T>) entityManager.createQuery(select)
                    .setMaxResults(previousquery.getLimit())
                    .getResultList();

            if (previousObjects.isEmpty()) {
                return null;
            }

            if (previousObjects.size() < query.getLimit()) {
                query.setLimit(previousObjects.size());
            }

            previousObject = previousObjects.get(previousObjects.size() - 1);
        } catch (Exception exception) {
            // do nothing
        }

        if (previousObject == null) {
            return null;
        }

        int previousCursor = 0;
        try {
            Field fieldNameField =
                previousObject.getClass().getDeclaredField("id");
            fieldNameField.setAccessible(true);
            Object fieldValue = fieldNameField.get(previousObject);

            previousCursor = (Integer) fieldValue;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // do nothing
        }

        if (previousCursor == 0) {
            return null;
        }

        return previousCursor;
    }

    private <R> void buildCriteriaBuilderSelect(CriteriaBuilder criteriaBuilder,
                                                CriteriaQuery<R> select,
                                                Query<S> query,
                                                Root<T> table) {
        List<Predicate> predicates = new ArrayList<>();

        int listIndex = 0;
        for (ColumnFilterList columnFilterList : query.getFilterList()) {
            for (ColumnFilter columnFilter : columnFilterList.getFilters()) {
                if (columnFilter.getProperty().contains(".")) {
                    String[] nestedProperties =
                        columnFilter.getProperty().split("\\.");

                    Path<?> path = table;
                    for (String nestedProperty : nestedProperties) {
                        path = path.get(nestedProperty);
                    }

                    predicates.add(
                        criteriaBuilder.equal(path, columnFilter.getValue()));

                    continue;
                }

                addPredicate(predicates, criteriaBuilder, table, columnFilter);
            }

            if (predicates.isEmpty()) continue;

            // For first filter list we add it with an OR conjunctive
            listIndex++;
            if (listIndex == 1 ||
                columnFilterList.getConjunctive() != QueryConjunctive.AND) {
                select.where(
                    criteriaBuilder.or(predicates.toArray(new Predicate[]{})));

                continue;
            }

            select.where(
                criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
        }
    }

    private CriteriaQuery<Object> buildQuery(Class<T> entity,
                                             Query<S> query) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();

        Root<T> table = criteriaQuery.from(entity);
        CriteriaQuery<Object> select = criteriaQuery.select(table);

        buildSortCriteria(criteriaBuilder, criteriaQuery, query, table);

        buildCriteriaBuilderSelect(criteriaBuilder, select, query, table);

        return select;
    }

    /**
     * Will determine what column type is and build
     * filter query based off type and provided filter
     */
    private void addPredicate(List<Predicate> predicates,
                              CriteriaBuilder criteriaBuilder,
                              Root<T> table, ColumnFilter columnFilter) {
        Path<Object> columnPath = table.get(columnFilter.getProperty());
        Class<?> columnType = columnPath.getJavaType();

        switch (columnFilter.getFilter()) {
            case EQ -> {
                if (columnType == Instant.class) {
                    if (GenericValidator.isDate(columnFilter.getValue(), "yyyy",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty()));

                        predicates.add(criteriaBuilder.equal(yearExpression,
                            columnFilter.getValue()));

                        return;
                    }

                    if (GenericValidator.isDate(columnFilter.getValue(),
                        "yyyy-mm",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty()));

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty()));

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.equal(yearExpression,
                                columnFilter.getValue().split("-")[0]),
                            criteriaBuilder.equal(monthExpression,
                                columnFilter.getValue().split("-")[1])
                        ));

                        return;
                    }

                    if (GenericValidator.isDate(columnFilter.getValue(),
                        "yyyy-mm-dd",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> dayExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("DAY"),
                                table.get(columnFilter.getProperty())
                            );

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.equal(yearExpression,
                                columnFilter.getValue()
                                    .split("-")[0]
                            ),
                            criteriaBuilder.equal(monthExpression,
                                columnFilter.getValue()
                                    .split("-")[1]
                            ),
                            criteriaBuilder.equal(
                                dayExpression,
                                columnFilter.getValue()
                                    .split("-")[2]
                            )
                        ));

                        return;
                    }

                    if (stringParser.isValidDateTimeHour(
                        columnFilter.getValue())) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> dayExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("DAY"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> hourExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("HOUR"),
                                table.get(columnFilter.getProperty())
                            );

                        String[] dateTimeParts =
                            columnFilter.getValue().split("T");
                        String[] dateParts = dateTimeParts[0].split("-");
                        int year = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]);
                        int day = Integer.parseInt(dateParts[2]);
                        int hour =
                            Integer.parseInt(dateTimeParts[1].substring(0, 2));

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.equal(yearExpression, year),
                            criteriaBuilder.equal(monthExpression, month),
                            criteriaBuilder.equal(dayExpression, day),
                            criteriaBuilder.equal(hourExpression, hour)
                        ));

                        return;
                    }

                    if (stringParser.isValidDateTimeHourAndMinutes(
                        columnFilter.getValue())) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> dayExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("DAY"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> hourExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("HOUR"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> minutesExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MINUTE"),
                                table.get(columnFilter.getProperty())
                            );

                        String[] dateTimeParts =
                            columnFilter.getValue().split("T");
                        String[] dateParts = dateTimeParts[0].split("-");
                        int year = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]);
                        int day = Integer.parseInt(dateParts[2]);
                        int hour =
                            Integer.parseInt(dateTimeParts[1].substring(0, 2));
                        int minutes =
                            Integer.parseInt(dateTimeParts[1].substring(3, 5));

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.equal(yearExpression, year),
                            criteriaBuilder.equal(monthExpression, month),
                            criteriaBuilder.equal(dayExpression, day),
                            criteriaBuilder.equal(hourExpression, hour),
                            criteriaBuilder.equal(minutesExpression, minutes)
                        ));

                        return;
                    }

                    if (stringParser.isValidDateTimeHourAndMinutesAndSeconds(
                        columnFilter.getValue())) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> dayExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("DAY"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> hourExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("HOUR"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> minutesExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MINUTE"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> secondsExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("SECOND"),
                                table.get(columnFilter.getProperty())
                            );

                        String[] dateTimeParts =
                            columnFilter.getValue().split("T");
                        String[] dateParts = dateTimeParts[0].split("-");
                        int year = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]);
                        int day = Integer.parseInt(dateParts[2]);
                        int hour =
                            Integer.parseInt(dateTimeParts[1].substring(0, 2));
                        int minutes =
                            Integer.parseInt(dateTimeParts[1].substring(3, 5));
                        int seconds =
                            Integer.parseInt(dateTimeParts[1].substring(6, 8));

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.equal(yearExpression, year),
                            criteriaBuilder.equal(monthExpression, month),
                            criteriaBuilder.equal(dayExpression, day),
                            criteriaBuilder.equal(hourExpression, hour),
                            criteriaBuilder.equal(minutesExpression, minutes),
                            criteriaBuilder.equal(secondsExpression, seconds)
                        ));

                        return;
                    }

                    if (stringParser.isValidDateTimeHourAndMinutesAndSecondsAndMilliseconds(
                        columnFilter.getValue())) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> dayExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("DAY"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> hourExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("HOUR"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> minutesExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MINUTE"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> millisecondsExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MILLISECONDS"),
                                table.get(columnFilter.getProperty())
                            );

                        String[] dateTimeParts =
                            columnFilter.getValue().split("T");
                        String[] dateParts = dateTimeParts[0].split("-");
                        int year = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]);
                        int day = Integer.parseInt(dateParts[2]);
                        int hour =
                            Integer.parseInt(dateTimeParts[1].substring(0, 2));
                        int minutes =
                            Integer.parseInt(dateTimeParts[1].substring(3, 5));
                        int milliseconds =
                            Integer.parseInt(dateTimeParts[1].substring(6, 12)
                                .replace(".", ""));

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.equal(yearExpression, year),
                            criteriaBuilder.equal(monthExpression, month),
                            criteriaBuilder.equal(dayExpression, day),
                            criteriaBuilder.equal(hourExpression, hour),
                            criteriaBuilder.equal(minutesExpression, minutes),
                            criteriaBuilder.equal(millisecondsExpression,
                                milliseconds)
                        ));

                        return;
                    }

                    throw new InvalidDateFormatException(
                        columnFilter.getProperty());
                }

                if (columnType == Integer.class) {
                    predicates.add(
                        criteriaBuilder.equal(
                            table.get(columnFilter.getProperty())
                                .as(Integer.class),
                            Integer.parseInt(columnFilter.getValue())));

                    return;
                }

                predicates.add(
                    criteriaBuilder.equal(
                        table.get(columnFilter.getProperty())
                            .as(String.class),
                        columnFilter.getValue()));
            }
            case NE -> {
                if (columnType == Instant.class) {
                    if (GenericValidator.isDate(columnFilter.getValue(), "yyyy",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty()));

                        predicates.add(criteriaBuilder.notEqual(yearExpression,
                            columnFilter.getValue()));

                        return;
                    }

                    if (GenericValidator.isDate(columnFilter.getValue(),
                        "yyyy-mm",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty()));

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty()));

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.notEqual(yearExpression,
                                columnFilter.getValue().split("-")[0]),
                            criteriaBuilder.notEqual(monthExpression,
                                columnFilter.getValue().split("-")[1])
                        ));

                        return;
                    }

                    if (GenericValidator.isDate(columnFilter.getValue(),
                        "yyyy-mm-dd",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> dayExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("DAY"),
                                table.get(columnFilter.getProperty())
                            );

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.notEqual(yearExpression,
                                columnFilter.getValue().split("-")[0]
                            ),
                            criteriaBuilder.notEqual(monthExpression,
                                columnFilter.getValue().split("-")[1]
                            ),
                            criteriaBuilder.notEqual(
                                dayExpression,
                                columnFilter.getValue().split("-")[2]
                            )
                        ));

                        return;
                    }

                    throw new InvalidDateFormatException(
                        columnFilter.getProperty());
                }

                if (columnType == Integer.class) {
                    predicates.add(
                        criteriaBuilder.notEqual(
                            table.get(columnFilter.getProperty())
                                .as(Integer.class),
                            Integer.parseInt(columnFilter.getValue())));

                    return;
                }

                predicates.add(
                    criteriaBuilder.notEqual(
                        table.get(columnFilter.getProperty())
                            .as(String.class),
                        columnFilter.getValue()));
            }
            case CURSOR, GTE -> {
                if (columnType == Instant.class) {
                    if (GenericValidator.isDate(columnFilter.getValue(), "yyyy",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty()));

                        predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(
                                yearExpression,
                                Integer.parseInt(
                                    columnFilter.getValue())));

                        return;
                    }

                    if (GenericValidator.isDate(columnFilter.getValue(),
                        "yyyy-mm",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty()));

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty()));

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.greaterThanOrEqualTo(
                                yearExpression,
                                Integer.parseInt(
                                    columnFilter.getValue()
                                        .split("-")[0])),
                            criteriaBuilder.greaterThanOrEqualTo(
                                monthExpression,
                                Integer.parseInt(
                                    columnFilter.getValue()
                                        .split("-")[1])
                            )
                        ));

                        return;
                    }

                    if (GenericValidator.isDate(columnFilter.getValue(),
                        "yyyy-mm-dd",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> dayExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("DAY"),
                                table.get(columnFilter.getProperty())
                            );

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.greaterThanOrEqualTo(
                                yearExpression,
                                Integer.parseInt(
                                    columnFilter.getValue()
                                        .split("-")[0])
                            ),
                            criteriaBuilder.greaterThanOrEqualTo(
                                monthExpression,
                                Integer.parseInt(
                                    columnFilter.getValue()
                                        .split("-")[1])
                            ),
                            criteriaBuilder.greaterThanOrEqualTo(
                                dayExpression,
                                Integer.parseInt(
                                    columnFilter.getValue()
                                        .split("-")[2])
                            )
                        ));

                        return;
                    }

                    throw new InvalidDateFormatException(
                        columnFilter.getProperty());
                }

                if (columnType == Integer.class) {
                    predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                            table.get(columnFilter.getProperty())
                                .as(Integer.class),
                            Integer.parseInt(columnFilter.getValue())));

                    return;
                }

                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        table.get(columnFilter.getProperty())
                            .as(String.class),
                        columnFilter.getValue()));
            }
            case GT -> {
                if (columnType == Instant.class) {
                    if (GenericValidator.isDate(columnFilter.getValue(), "yyyy",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty()));

                        predicates.add(
                            criteriaBuilder.greaterThan(yearExpression,
                                Integer.parseInt(
                                    columnFilter.getValue())));

                        return;
                    }

                    if (GenericValidator.isDate(columnFilter.getValue(),
                        "yyyy-mm",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty()));

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty()));

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.greaterThan(yearExpression,
                                Integer.parseInt(columnFilter.getValue()
                                    .split("-")[0]))
                            ,
                            criteriaBuilder.greaterThan(monthExpression,
                                Integer.parseInt(columnFilter.getValue()
                                    .split("-")[1]))
                        ));

                        return;
                    }

                    if (GenericValidator.isDate(columnFilter.getValue(),
                        "yyyy-mm-dd",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> dayExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("DAY"),
                                table.get(columnFilter.getProperty())
                            );

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.greaterThan(yearExpression,
                                Integer.parseInt(columnFilter.getValue()
                                    .split("-")[0]))
                            ,
                            criteriaBuilder.greaterThan(monthExpression,
                                Integer.parseInt(columnFilter.getValue()
                                    .split("-")[1])
                            ),
                            criteriaBuilder.greaterThan(
                                dayExpression,
                                Integer.parseInt(columnFilter.getValue()
                                    .split("-")[2])
                            )
                        ));

                        return;
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
                    if (stringParser.isValidDateTimeHourAndMinutesAndSecondsAndMilliseconds(
                        columnFilter.getValue())) {
                        LocalDateTime localDateTime =
                            LocalDateTime.parse(columnFilter.getValue(),
                                formatter);

                        predicates.add(
                            criteriaBuilder.greaterThan(
                                table.get(columnFilter.getProperty())
                                    .as(Timestamp.class),
                                Timestamp.valueOf(localDateTime)));

                        return;
                    }

                    if (stringParser.isISO8601(columnFilter.getValue())) {
                        predicates.add(
                            criteriaBuilder.greaterThan(
                                table.get(columnFilter.getProperty())
                                    .as(Instant.class),
                                Instant.from(
                                    formatter.parse(columnFilter.getValue())
                                )
                            )
                        );

                        return;
                    }

                    throw new InvalidDateFormatException(
                        columnFilter.getProperty());
                }

                if (columnType == Integer.class) {
                    predicates.add(
                        criteriaBuilder.greaterThan(
                            table.get(columnFilter.getProperty())
                                .as(Integer.class),
                            Integer.parseInt(columnFilter.getValue())));

                    return;
                }

                predicates.add(
                    criteriaBuilder.greaterThan(
                        table.get(columnFilter.getProperty())
                            .as(String.class),
                        columnFilter.getValue()));
            }
            case LTE -> {
                if (columnType == Instant.class) {
                    if (GenericValidator.isDate(columnFilter.getValue(), "yyyy",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty()));

                        predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(
                                yearExpression,
                                Integer.parseInt(
                                    columnFilter.getValue())));
                        return;
                    }

                    if (GenericValidator.isDate(columnFilter.getValue(),
                        "yyyy-mm",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty()));

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty()));

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.lessThanOrEqualTo(
                                yearExpression,
                                Integer.parseInt(
                                    columnFilter.getValue()
                                        .split("-")[0])),
                            criteriaBuilder.lessThanOrEqualTo(
                                monthExpression,
                                Integer.parseInt(
                                    columnFilter.getValue()
                                        .split("-")[1])
                            )
                        ));

                        return;
                    }

                    if (GenericValidator.isDate(columnFilter.getValue(),
                        "yyyy-mm-dd",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> dayExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("DAY"),
                                table.get(columnFilter.getProperty())
                            );

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.lessThanOrEqualTo(
                                yearExpression,
                                Integer.parseInt(
                                    columnFilter.getValue()
                                        .split("-")[0])
                            ),
                            criteriaBuilder.lessThanOrEqualTo(
                                monthExpression,
                                Integer.parseInt(
                                    columnFilter.getValue()
                                        .split("-")[1])
                            ),
                            criteriaBuilder.lessThanOrEqualTo(
                                dayExpression,
                                Integer.parseInt(
                                    columnFilter.getValue()
                                        .split("-")[2])
                            )
                        ));

                        return;
                    }

                    throw new InvalidDateFormatException(
                        columnFilter.getProperty());
                }

                if (columnType == Integer.class) {
                    predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                            table.get(columnFilter.getProperty())
                                .as(Integer.class),
                            Integer.parseInt(columnFilter.getValue())));

                    return;
                }

                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        table.get(columnFilter.getProperty())
                            .as(String.class),
                        columnFilter.getValue()));
            }
            case LT -> {
                if (columnType == Instant.class) {
                    if (GenericValidator.isDate(columnFilter.getValue(), "yyyy",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty()));

                        predicates.add(
                            criteriaBuilder.lessThan(yearExpression,
                                Integer.parseInt(
                                    columnFilter.getValue())));

                        return;
                    }

                    if (GenericValidator.isDate(columnFilter.getValue(),
                        "yyyy-mm",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty()));

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty()));

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.lessThan(yearExpression,
                                Integer.parseInt(columnFilter.getValue()
                                    .split("-")[0]))
                            ,
                            criteriaBuilder.lessThan(monthExpression,
                                Integer.parseInt(columnFilter.getValue()
                                    .split("-")[1]))
                        ));

                        return;
                    }

                    if (GenericValidator.isDate(columnFilter.getValue(),
                        "yyyy-mm-dd",
                        true)) {
                        Expression<Integer> yearExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("YEAR"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> monthExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("MONTH"),
                                table.get(columnFilter.getProperty())
                            );

                        Expression<Integer> dayExpression =
                            criteriaBuilder.function("date_part",
                                Integer.class,
                                criteriaBuilder.literal("DAY"),
                                table.get(columnFilter.getProperty())
                            );

                        predicates.add(criteriaBuilder.and(
                            criteriaBuilder.lessThan(yearExpression,
                                Integer.parseInt(columnFilter.getValue()
                                    .split("-")[0]))
                            ,
                            criteriaBuilder.lessThan(monthExpression,
                                Integer.parseInt(columnFilter.getValue()
                                    .split("-")[1])
                            ),
                            criteriaBuilder.lessThan(
                                dayExpression,
                                Integer.parseInt(columnFilter.getValue()
                                    .split("-")[2])
                            )
                        ));

                        return;
                    }

                    throw new InvalidDateFormatException(
                        columnFilter.getProperty());
                }

                if (columnType == Integer.class) {
                    predicates.add(
                        criteriaBuilder.lessThan(
                            table.get(columnFilter.getProperty())
                                .as(Integer.class),
                            Integer.parseInt(columnFilter.getValue())));

                    return;
                }

                predicates.add(
                    criteriaBuilder.lessThan(
                        table.get(columnFilter.getProperty())
                            .as(String.class),
                        columnFilter.getValue()));
            }
            case LIKE -> {
                predicates.add(
                    criteriaBuilder.like(
                        table.get(columnFilter.getProperty())
                            .as(String.class),
                        "%" + columnFilter.getValue() +
                            "%"));
            }
            default -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The filter " + columnFilter.getFilter() +
                        " isn't supported.");
            }
        }
    }

    private void buildSortCriteria(CriteriaBuilder criteriaBuilder,
                                   CriteriaQuery<Object> criteriaQuery,
                                   Query<S> query,
                                   Root<T> table) {
        HashMap<QuerySort, String[]> sortList = query.getSortList();
        if (sortList == null) return;

        String[] ascSortList = query.getSortList().get(QuerySort.ASC);
        if (ascSortList != null) {
            for (String ascSort : ascSortList) {
                criteriaQuery.orderBy(criteriaBuilder.asc(table.get(ascSort)));
            }
        }

        String[] descSortList = query.getSortList().get(QuerySort.DESC);
        if (descSortList != null) {
            for (String descSort : descSortList) {
                criteriaQuery.orderBy(
                    criteriaBuilder.desc(table.get(descSort)));
            }
        }
    }
}
