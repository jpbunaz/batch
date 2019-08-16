package io.balteusit.framework.batch.execute.service;

import io.balteusit.framework.batch.core.BatchProperties;
import io.balteusit.framework.batch.execute.domain.ExecutableInterface;
import io.balteusit.framework.batch.execute.domain.Execution;
import io.balteusit.framework.batch.execute.domain.ExecutionStatus;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LaunchService {

  private Logger logger = LoggerFactory.getLogger(LaunchService.class);

  private BatchProperties batchProperties = BatchProperties.getInstance();

  private int threadNumber = Integer.valueOf(batchProperties.getProperty("thread.number", "4"));

  private ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);

  private ExecutableService executableService = ExecutableService.getInstance();

  private ExecutionService executionService = ExecutionService.getInstance();


  private static LaunchService launchService = new LaunchService();

  private LaunchService() {
  }

  public static LaunchService getInstance() {
    return launchService;
  }

  @SuppressWarnings("unchecked")
  public ExecutionStatus launch(Long executableId) {
    return executableService.getById(executableId).map(executable -> {
      String id = executable.getClassName();
      ExecutableInterface executableInterface;
      try {
        Class<ExecutableInterface> aClass = (Class<ExecutableInterface>) Class.forName(id);
        executableInterface = aClass.newInstance();
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        logger.error(e.getMessage());
        return ExecutionStatus.START_FAILED;
      }

      ExecutionStatus executionStatus = ExecutionStatus.RUNNING;
      Execution execution = new Execution();
      execution.setStartDate(new Date());
      execution.setStatus(executionStatus);
      execution.setExecutable(executable);
      executionService.save(execution);

      try {
        executionStatus = executableInterface.perform();
      } catch (Exception e) {
        executionStatus = ExecutionStatus.FAILED;
        logger.error("Error during execution", e);
      } finally {
        execution.setStatus(executionStatus);
        execution.setEndDate(new Date());
        executableInterface.getLog().ifPresent(execution::setLog);
        executionService.save(execution);
      }
      return executionStatus;
    }).orElse(ExecutionStatus.START_FAILED);
  }

  public Future<ExecutionStatus> launchAsync(Long executableId) {
    return executorService.submit(() -> launch(executableId));
  }


}
