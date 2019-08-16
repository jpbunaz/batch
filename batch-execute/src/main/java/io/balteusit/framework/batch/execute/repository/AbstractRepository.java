package io.balteusit.framework.batch.execute.repository;

import io.balteusit.framework.batch.core.BatchProperties;
import io.balteusit.framework.batch.execute.common.PageableList;
import io.balteusit.framework.batch.execute.exception.DatabaseException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class AbstractRepository<T, I> {

  protected static EntityManager entityManager;

  static {
    EntityManagerFactory bdd = Persistence.createEntityManagerFactory("bdd", BatchProperties.getInstance());
    entityManager = bdd.createEntityManager();
  }

  private Class<T> aClass;

  public Optional<T> getById(I id) {
    Class<T> aClass = getEntityClass();
    return Optional.ofNullable(entityManager.find(aClass, id));
  }

  @SuppressWarnings("unchecked")
  private Class<T> getEntityClass() {
    if (aClass == null) {
      Type sooper = getClass().getGenericSuperclass();
      Type t = ((ParameterizedType) sooper).getActualTypeArguments()[0];
      try {
        aClass = (Class<T>) Class.forName(t.getTypeName());
      } catch (ClassNotFoundException e) {
        throw new DatabaseException(e);
      }
    }
    return aClass;
  }

  public void persist(T entity) {
    entityManager.persist(entity);
  }

  public void persistNow(T entity) {
    executeAroundTransaction(() -> entityManager.persist(entity));
  }

  public void remove(I id) {
    getById(id).ifPresent(toDelete -> entityManager.remove(toDelete));
  }

  public void removeNow(I id) {
    executeAroundTransaction(() -> remove(id));
  }

  private EntityTransaction getTransaction() {
    return entityManager.getTransaction();
  }

  public PageableList<T> findAll(int page, int size, Order... orders) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();

    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    CriteriaQuery<Long> select1 = countQuery.select(cb.count(countQuery.from(this.getEntityClass())));
    Long count = entityManager.createQuery(select1).getSingleResult();

    TypedQuery<T> query = findAllTypedQuery(cb, orders);

    return execute(page, size, count, query);
  }

  public List<T> findAll(Order... orders) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    TypedQuery<T> query = findAllTypedQuery(cb, orders);
    return query.getResultList();
  }

  private TypedQuery<T> findAllTypedQuery(CriteriaBuilder cb, Order... orders) {
    CriteriaQuery<T> criteriaQuery = cb.createQuery(this.getEntityClass());
    Root<T> from = criteriaQuery.from(this.getEntityClass());
    CriteriaQuery<T> select = criteriaQuery.select(from);

    List<javax.persistence.criteria.Order> collect = Arrays.stream(orders).map(order -> {
      if (order.isAsc()) {
        return cb.asc(from.get(order.getField()));
      } else {
        return cb.desc(from.get(order.getField()));
      }
    }).collect(Collectors.toList());
    select.orderBy(collect);

    return entityManager.createQuery(select);
  }

  protected <T> PageableList<T> execute(int page, int size, Long count, TypedQuery<T> query) {
    query.setFirstResult(page * size);
    query.setMaxResults(size);
    List<T> resultList = query.getResultList();
    return new PageableList<>(page, size, count, resultList);
  }

  public int executeAroundTransaction(Supplier<Integer> supplier) {
    EntityTransaction transaction = this.getTransaction();
    transaction.begin();
    try {
      Integer toReturn = supplier.get();
      transaction.commit();
      return toReturn;
    } catch (Exception e) {
      transaction.rollback();
      throw e;
    }
  }

  public void executeAroundTransaction(Runnable runnable) {
    EntityTransaction transaction = this.getTransaction();
    transaction.begin();
    try {
      runnable.run();
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      throw e;
    }
  }

}
