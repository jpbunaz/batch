package io.balteusit.framework.batch.execute.service;

import static io.balteusit.framework.batch.core.util.DateUtils.toDate;
import static io.balteusit.framework.batch.execute.domain.ExecutionStatus.RUNNING;
import static org.assertj.core.api.Assertions.assertThat;

import io.balteusit.framework.batch.execute.common.AbstractDatabaseTest;
import io.balteusit.framework.batch.execute.common.PageableList;
import io.balteusit.framework.batch.execute.domain.Executable;
import io.balteusit.framework.batch.execute.domain.Execution;
import io.balteusit.framework.batch.execute.domain.ExecutionStatus;
import io.balteusit.framework.batch.execute.repository.ExecutableRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class ExecutionServiceTest extends AbstractDatabaseTest {

  @DisplayName("Find by executable id and execution with running status are updated to aborted on init")
  @Test
  @Order(1)
  void testFindAndAbortedStatus() {
    Long id = ExecutableRepository.getInstance().findByClassName(ExecutableExample4.class.getName()).get(0).getId();

    ExecutionService executionService = ExecutionService.getInstance();

    PageableList<Execution> executions = executionService.findByExecutableId(id, 0, 100);

    assertThat(executions.getContent())
        .hasSize(2)
        .extracting("status")
        .doesNotContain(RUNNING);
  }

  @DisplayName("Find by id")
  @Test
  @Order(2)
  void testFindById() {
    Long id = ExecutableRepository.getInstance().findByClassName(ExecutableExample4.class.getName()).get(0).getId();

    ExecutionService executionService = ExecutionService.getInstance();

    PageableList<Execution> executions = executionService.findByExecutableId(id, 0, 100);
    Execution execution = executions.getContent().get(1);

    Optional<Execution> executionById = executionService.getById(execution.getId());

    assertThat(executionById)
        .isPresent()
        .get()
        .isEqualToComparingFieldByField(execution);
  }

  @DisplayName("Delete by id")
  @Test
  @Order(3)
  void testRemove() {
    Long id = ExecutableRepository.getInstance().findByClassName(ExecutableExample4.class.getName()).get(0).getId();

    ExecutionService executionService = ExecutionService.getInstance();

    PageableList<Execution> executions = executionService.findByExecutableId(id, 0, 100);
    Execution execution = executions.getContent().get(1);

    executionService.remove(execution.getId());

    Optional<Execution> executionById = executionService.getById(execution.getId());

    assertThat(executionById).isNotPresent();
  }

  @DisplayName("Save")
  @Test
  @Order(4)
  void testSave() {
    Executable executable = ExecutableRepository.getInstance().findByClassName(ExecutableExample4.class.getName()).get(0);
    Execution executionToSave = new Execution(
        toDate("20101005235205"),
        toDate("20101006002345"),
        ExecutionStatus.FAILED,
        executable,
        "Ceci est un message de log"
    );

    ExecutionService executionService = ExecutionService.getInstance();
    executionService.save(executionToSave);

    assertThat(executionToSave.getId()).isNotNull();

    Optional<Execution> executionFromDatabase = executionService.getById(executionToSave.getId());
    assertThat(executionFromDatabase).isPresent().get().isEqualToComparingFieldByFieldRecursively(executionToSave);
  }
}