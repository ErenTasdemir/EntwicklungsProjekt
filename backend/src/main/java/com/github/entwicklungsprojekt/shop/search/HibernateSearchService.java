package com.github.entwicklungsprojekt.shop.search;

import com.github.entwicklungsprojekt.shop.persistence.Shop;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Hibernate search service.
 */
@Slf4j
@Transactional
@Service
public class HibernateSearchService {

    /**
     * The Entity manager.
     */
    @PersistenceContext
    EntityManager entityManager;

    private final Map<Class<?>, List<String>> classIndexedFieldMap;

    /**
     * Instantiates a new Hibernate search service.
     */
    public HibernateSearchService() {
        this.classIndexedFieldMap = new HashMap<>();
    }

    /**
     * Search shops list.
     *
     * @param simpleQueryString the simple query string
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<Shop> searchShops(@NotNull String simpleQueryString) {
        var query = getShopBaseQuery(simpleQueryString);

        return getFullTextEntityManager().createFullTextQuery(query, Shop.class)
                .getResultList();
    }

    private Query getShopBaseQuery(String simpleQueryString) {
        var queryBuilder = getQueryBuilder(Shop.class);

        var baseQuery = getQuerySearchingForAllIndexedFields(Shop.class, simpleQueryString);

        return queryBuilder.bool()
                .must(baseQuery)
                .createQuery();
    }

    private boolean isIndexedEntity(Class<?> entityType) {
        return Objects.nonNull(entityType.getAnnotation(Entity.class)) &&
                Objects.nonNull(entityType.getAnnotation(Indexed.class));
    }

    private List<String> getNamesOfAnnotatedStringFields(Class<?> entityType) {
        if(classIndexedFieldMap.containsKey(entityType)) {
            return classIndexedFieldMap.get(entityType);
        }

        var indexedStringFields = Arrays.stream(entityType.getDeclaredFields())
                .filter(field -> String.class.equals(field.getType()) && Objects.nonNull(field.getAnnotation(Field.class)))
                .map(field -> {
                    var fieldAnnotation = field.getAnnotation(Field.class);

                    if(!fieldAnnotation.name().isEmpty()) {
                        return fieldAnnotation.name();
                    }

                    return field.getName();
                })
                .collect(Collectors.toList());
        classIndexedFieldMap.put(entityType, indexedStringFields);

        return indexedStringFields;
    }

    private Query getQuerySearchingForAllIndexedFields(Class<?> entityType, String simpleQueryString) {
        if(!isIndexedEntity(entityType)) {
            throw new IllegalArgumentException("Given type is not an entity or is not indexed!");
        }

        var annotatedStringFields = getNamesOfAnnotatedStringFields(entityType);
        if(annotatedStringFields.isEmpty()) {
            throw new IllegalArgumentException("No field of type String annotated with @Field!");
        }

        var fuzzyAndPrefixQuery = HibernateSimpleQueryUtils.makeQueryPrefixAndFuzzy(simpleQueryString);

        var queryBuilder = getQueryBuilder(entityType);
        return queryBuilder.simpleQueryString()
                .onFields(annotatedStringFields.get(0), annotatedStringFields.subList(1, annotatedStringFields.size()).toArray(String[]::new))
                .withAndAsDefaultOperator()
                .matching(fuzzyAndPrefixQuery)
                .createQuery();
    }

    private QueryBuilder getQueryBuilder(Class<?> type) {
        return getFullTextEntityManager().getSearchFactory()
                .buildQueryBuilder()
                .forEntity(type)
                .get();
    }

    private FullTextEntityManager getFullTextEntityManager() {
        return Search.getFullTextEntityManager(entityManager);
    }

    /**
     * Index existing entities.
     *
     * @param initEntityManager the init entity manager
     */
    void indexExistingEntities(EntityManager initEntityManager) {
        try {
            Search.getFullTextEntityManager(initEntityManager)
                    .createIndexer()
                    .startAndWait();

            log.info("Successfully built lucene index!");
        } catch (InterruptedException ex) {
            log.error("Error building lucene index!", ex);
        }
    }

}
