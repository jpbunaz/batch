package io.balteusit.framework.batch.execute.repository;

import io.balteusit.framework.batch.execute.domain.Executable;
import java.util.List;
import javax.persistence.TypedQuery;

public class ExecutableRepository extends AbstractRepository<Executable, Long> {

  private static ExecutableRepository executableRepository = new ExecutableRepository();

  public static ExecutableRepository getInstance() {
    return executableRepository;
  }

  public List<Executable> findByClassName(String className) {
    TypedQuery<Executable> query = entityManager.createQuery("select e from Executable e where e.className = :className", Executable.class);
    query.setParameter("className", className);

    return query.getResultList();
  }

}
