package io.balteusit.framework.batch.execute.service;

import static io.balteusit.framework.batch.execute.exception.RemoveExecutableException.MESSAGE_EXISTING_EXECUTION;
import static io.balteusit.framework.batch.execute.exception.RemoveExecutableException.MESSAGE_RUNNING_EXECUTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.groups.Tuple.tuple;

import io.balteusit.framework.batch.execute.common.AbstractDatabaseTest;
import io.balteusit.framework.batch.execute.common.PageableList;
import io.balteusit.framework.batch.execute.domain.Executable;
import io.balteusit.framework.batch.execute.exception.RemoveExecutableException;
import io.balteusit.framework.batch.execute.repository.ExecutableRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class ExecutableServiceTest extends AbstractDatabaseTest {


  @DisplayName("Test find and executable has been register")
  @Test
  @Order(1)
  public void testFindAndExecutable() {
    ExecutableService executableService = ExecutableService.getInstance();

    PageableList<Executable> executables = executableService
        .getExecutables(0, 4, new io.balteusit.framework.batch.execute.repository.Order("className", false));

    assertThat(executables.getTotal()).isEqualTo(6l);
    assertThat(executables.getPage()).isEqualTo(0);
    assertThat(executables.getSize()).isEqualTo(4);

    assertThat(executables.getContent())
        .hasSize(4)
        .extracting("className", "name", "asynchronous", "active")
        .contains(
            tuple("io.balteusit.framework.batch.execute.service.ExecutableExample6", "ExecutableExample6", true, false),
            tuple("io.balteusit.framework.batch.execute.service.ExecutableExample5", "ExecutableExample5", true, true)
        );
  }

  @DisplayName("Test get by id")
  @Test
  @Order(2)
  public void testGetById() {
    Long id = ExecutableRepository.getInstance().findByClassName(ExecutableExample4.class.getName()).get(0).getId();

    ExecutableService executableService = ExecutableService.getInstance();

    Optional<Executable> executableOptional = executableService.getById(id);

    assertThat(executableOptional).isPresent().get()
        .hasFieldOrPropertyWithValue("className", ExecutableExample4.class.getName())
        .hasFieldOrPropertyWithValue("name", ExecutableExample4.class.getSimpleName())
        .hasFieldOrPropertyWithValue("asynchronous", true)
        .hasFieldOrPropertyWithValue("active", true);
  }

  @DisplayName("Test update executable")
  @Test
  @Order(3)
  void testUpdateExecutable() {

    Long id = ExecutableRepository.getInstance().findByClassName(ExecutableExample1.class.getName()).get(0).getId();

    ExecutableService executableService = ExecutableService.getInstance();
    String aNewName = "A new Name";
    Executable executable = new Executable(id, "io.balteusit.framework.batch.execute.service.ExecutableExample1", aNewName, true, false);

    executableService.saveOrUpdate(executable);

    Optional<Executable> executableAfterUpdateOpt = executableService.getById(id);
    assertThat(executableAfterUpdateOpt).isPresent().get()
        .isEqualToComparingFieldByField(
            new Executable(id, "io.balteusit.framework.batch.execute.service.ExecutableExample1", aNewName, true, false));
  }

  @DisplayName("Test can't delete an executable with running execution")
  @Test
  @Order(4)
  void testDeleteRunning() {
    Long id = ExecutableRepository.getInstance().findByClassName(ExecutableExample4.class.getName()).get(0).getId();

    ExecutableService executableService = ExecutableService.getInstance();

    Throwable throwable = catchThrowable(() -> executableService.deleteExecutable(id, true));

    assertThat(throwable).isInstanceOf(RemoveExecutableException.class).hasMessage(MESSAGE_RUNNING_EXECUTION);
  }

  @DisplayName("Test soft delete : Only Executable without execution")
  @Test
  @Order(5)
  void testDeleteSoft() {
    Long id = ExecutableRepository.getInstance().findByClassName(ExecutableExample5.class.getName()).get(0).getId();

    ExecutableService executableService = ExecutableService.getInstance();

    Throwable throwable = catchThrowable(() -> executableService.deleteExecutable(id, false));

    assertThat(throwable).isInstanceOf(RemoveExecutableException.class).hasMessage(MESSAGE_EXISTING_EXECUTION);

    Long id3 = ExecutableRepository.getInstance().findByClassName(ExecutableExample3.class.getName()).get(0).getId();

    executableService.deleteExecutable(id3, false);

    Optional<Executable> executable = executableService.getById(id3);
    assertThat(executable).isNotPresent();
  }

  @DisplayName("Test hard delete : Delete execution before executable")
  @Test
  @Order(6)
  void testDeleteHard() {
    Long id = ExecutableRepository.getInstance().findByClassName(ExecutableExample5.class.getName()).get(0).getId();

    ExecutableService executableService = ExecutableService.getInstance();

    executableService.deleteExecutable(id, true);

    Optional<Executable> executable = executableService.getById(id);
    assertThat(executable).isNotPresent();
  }
}