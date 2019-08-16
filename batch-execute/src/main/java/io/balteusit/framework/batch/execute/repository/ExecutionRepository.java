package io.balteusit.framework.batch.execute.repository;

import io.balteusit.framework.batch.execute.common.PageableList;
import io.balteusit.framework.batch.execute.domain.Execution;
import io.balteusit.framework.batch.execute.domain.ExecutionStatus;
import java.util.Date;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class ExecutionRepository extends AbstractRepository<Execution, Long> {

  private static ExecutionRepository executionRepository = new ExecutionRepository();

  public static ExecutionRepository getInstance() {
    return executionRepository;
  }

  public PageableList<Execution> findByExecutableId(Long id, int page, int size) {
    Long count = countByExecutableId(id);

    TypedQuery<Execution> query = entityManager.createQuery("select e from Execution e where e.executable.id = :id order by e.startDate desc", Execution.class);
    query.setParameter("id", id);

    return execute(page, size, count, query);
  }

  public int updateStatusAndEndDate(ExecutionStatus oldStatus, ExecutionStatus newStatus) {

    Query query = entityManager.createQuery("update Execution e set e.status = :newStatus, e.endDate = :endDate where e.status = :oldStatus");
    query.setParameter("oldStatus", oldStatus);
    query.setParameter("newStatus", newStatus);
    query.setParameter("endDate", new Date());

    return query.executeUpdate();
  }

  public Long countByExecutableId(Long id) {
    TypedQuery<Long> countQuery = entityManager.createQuery("select count(e) from Execution e where e.executable.id = :id", Long.class);
    countQuery.setParameter("id", id);
    return countQuery.getSingleResult();
  }

  public int deleteByExecutableId(Long id) {
    Query query = entityManager.createQuery("delete from Execution e where e.executable.id = :id");
    query.setParameter("id", id);
    return query.executeUpdate();
  }

  public long countByExecutableIdAndStatus(Long id, ExecutionStatus status) {
    TypedQuery<Long> countQuery = entityManager
        .createQuery("select count(e) from Execution e where e.executable.id = :id and e.status = :status", Long.class);
    countQuery.setParameter("id", id);
    countQuery.setParameter("status", status);
    return countQuery.getSingleResult();
  }

}
