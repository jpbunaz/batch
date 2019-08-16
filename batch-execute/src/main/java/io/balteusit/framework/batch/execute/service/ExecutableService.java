package io.balteusit.framework.batch.execute.service;

import static io.balteusit.framework.batch.execute.exception.RemoveExecutableException.MESSAGE_EXISTING_EXECUTION;
import static io.balteusit.framework.batch.execute.exception.RemoveExecutableException.MESSAGE_RUNNING_EXECUTION;

import io.balteusit.framework.batch.core.BatchProperties;
import io.balteusit.framework.batch.execute.common.PageableList;
import io.balteusit.framework.batch.execute.domain.Executable;
import io.balteusit.framework.batch.execute.domain.ExecutableInterface;
import io.balteusit.framework.batch.execute.domain.ExecutionStatus;
import io.balteusit.framework.batch.execute.exception.ExecutableRegisterException;
import io.balteusit.framework.batch.execute.exception.RemoveExecutableException;
import io.balteusit.framework.batch.execute.repository.ExecutableRepository;
import io.balteusit.framework.batch.execute.repository.ExecutionRepository;
import io.balteusit.framework.batch.execute.repository.Order;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutableService {

  private final Logger logger = LoggerFactory.getLogger(ExecutorService.class);

  private static ExecutableService executableService = new ExecutableService();

  private BatchProperties batchProperties = BatchProperties.getInstance();

  private ExecutableRepository executableRepository = ExecutableRepository.getInstance();

  private ExecutionRepository executionRepository = ExecutionRepository.getInstance();

  private ExecutableService() {
    this.register();
  }

  public static ExecutableService getInstance() {
    return executableService;
  }

  private void register() {
    executableRepository.executeAroundTransaction(() -> {
      List<Executable> executables = executableRepository.findAll();
      for (Executable executable : executables) {
        String className = executable.getClassName();
        try {
          Class.forName(className);
        } catch (ClassNotFoundException e) {
          executableRepository.remove(executable.getId());
        }
      }

      Reflections reflections = new Reflections(BatchProperties.getInstance().getProperty("executable.prefix", ""));
      Set<Class<? extends ExecutableInterface>> subTypesOf = reflections.getSubTypesOf(ExecutableInterface.class);
      for (Class<? extends ExecutableInterface> executableClass : subTypesOf) {
        List<Executable> executableFromBdd = executableRepository.findByClassName(executableClass.getName());
        if (executableFromBdd.isEmpty()) {
          ExecutableInterface executableInterface = initExecutable(executableClass);

          Executable executable = new Executable();
          executable.setClassName(executableClass.getName());
          executable.setName(executableClass.getSimpleName());
          executable.setAsynchronous(executableInterface.isAsynchronous());
          executable.setActive(executableInterface.isActive());

          executableRepository.persist(executable);
        }
      }
    });
  }

  private ExecutableInterface initExecutable(Class<? extends ExecutableInterface> executableClass) {
    try {
      return executableClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new ExecutableRegisterException(String.format("Error while registring %s"), e);
    }
  }

  public Optional<Executable> getById(Long id) {
    return executableRepository.getById(id);
  }

  public void saveOrUpdate(Executable executable) {
    if (executable.getId() != null) {
      executableRepository.getById(executable.getId()).ifPresent(executableFromBdd -> {
        executableFromBdd.setAsynchronous(executable.isAsynchronous());
        executableFromBdd.setName(executable.getName());
        executableFromBdd.setActive(executable.isActive());
        executableRepository.persistNow(executableFromBdd);
      });
    } else {
      executableRepository.persistNow(executable);
    }

  }

  public void deleteExecutable(Long id, boolean hard) {
    long count = executionRepository.countByExecutableIdAndStatus(id, ExecutionStatus.RUNNING);
    if (count == 0) {
      if (hard) {
        executableRepository.executeAroundTransaction(() -> {
          executionRepository.deleteByExecutableId(id);
          executableRepository.remove(id);
        });
      } else {
        Long executionById = executionRepository.countByExecutableId(id);
        if (executionById == 0) {
          executableRepository.remove(id);
        } else {
          throw new RemoveExecutableException(MESSAGE_EXISTING_EXECUTION);
        }
      }
    } else {
      throw new RemoveExecutableException(MESSAGE_RUNNING_EXECUTION);
    }
  }

  public PageableList<Executable> getExecutables(int page, int size, Order... orders) {
    return executableRepository.findAll(page, size, orders);
  }

}
