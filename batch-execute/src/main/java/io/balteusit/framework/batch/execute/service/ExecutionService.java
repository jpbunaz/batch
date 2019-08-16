package io.balteusit.framework.batch.execute.service;

import static io.balteusit.framework.batch.execute.domain.ExecutionStatus.ABORTED;
import static io.balteusit.framework.batch.execute.domain.ExecutionStatus.RUNNING;

import io.balteusit.framework.batch.execute.common.PageableList;
import io.balteusit.framework.batch.execute.domain.Execution;
import io.balteusit.framework.batch.execute.repository.ExecutionRepository;
import java.util.Optional;

public class ExecutionService {

  private ExecutionRepository executionRepository = ExecutionRepository.getInstance();

  private static ExecutionService executionService = new ExecutionService();

  private ExecutionService() {
    init();
  }

  private void init() {
    executionRepository.executeAroundTransaction(() -> executionRepository.updateStatusAndEndDate(RUNNING, ABORTED));
  }

  public static ExecutionService getInstance() {
    return executionService;
  }

  public PageableList<Execution> findByExecutableId(Long executableId, int page, int size) {
    return executionRepository.findByExecutableId(executableId, page, size);
  }

  public Optional<Execution> getById(long executionId) {
    return executionRepository.getById(executionId);
  }

  public void remove(long executionId) {
    executionRepository.removeNow(executionId);
  }


  public void save(Execution execution) {
    executionRepository.persistNow(execution);
  }
}
