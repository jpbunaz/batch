package io.balteusit.framework.batch.execute.service;

import static io.balteusit.framework.batch.execute.exception.RemoveExecutableException.MESSAGE_EXISTING_EXECUTION;
import static io.balteusit.framework.batch.execute.exception.RemoveExecutableException.MESSAGE_RUNNING_EXECUTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.groups.Tuple.tuple;

import io.balteusit.framework.batch.execute.common.AbstractDatabaseTest;
import io.balteusit.framework.batch.execute.common.PageableList;
import io.balteusit.framework.batch.execute.domain.Executable;
import io.balteusit.framework.batch.execute.domain.ExecutableJob;
import io.balteusit.framework.batch.execute.exception.NoJobClassExecutableJobException;
import io.balteusit.framework.batch.execute.exception.NotExistingJobClassExecutableJobException;
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


  @DisplayName("Find and executable has been register")
  @Test
  @Order(100)
  public void testFindAndExecutable() {
    ExecutableService executableService = ExecutableService.getInstance();

    PageableList<Executable> executables = executableService
        .getExecutables(0, 4, new io.balteusit.framework.batch.execute.repository.Order("className", false));

    assertThat(executables.getTotal()).isEqualTo(7l);
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

  @DisplayName("Get by id")
  @Test
  @Order(200)
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

  @DisplayName("Update executable")
  @Test
  @Order(300)
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

  @DisplayName("Create an executable job")
  @Test
  @Order(350)
  void testCreateExecutableJob() {

    Executable executable = new Executable(ExecutableJob.class.getName(), "My exectuable for test", true, true);
    executable.setJobClass(JobForTest.class.getName());

    ExecutableService executableService = ExecutableService.getInstance();
    executableService.saveOrUpdate(executable);

    Optional<Executable> executableBddOpt = executableService.getById(executable.getId());

    assertThat(executableBddOpt)
        .isPresent()
        .get()
        .isEqualToComparingFieldByField(executable);

  }

  @DisplayName("Can't create executable job without class")
  @Test
  @Order(360)
  void testCreateExeccutableJobWithoutClassJob() {

    Executable executable = new Executable(ExecutableJob.class.getName(), "My executable for test", true, true);

    ExecutableService executableService = ExecutableService.getInstance();
    Throwable throwable = catchThrowable(() -> executableService.saveOrUpdate(executable));

    assertThat(throwable).isInstanceOf(NoJobClassExecutableJobException.class);

  }

  @DisplayName("Can't create executable job without not existing class")
  @Test
  @Order(370)
  void testCreateExecutableJobWithNotExistingClassJob() {

    Executable executable = new Executable(ExecutableJob.class.getName(), "My executable for test", true, true);
    executable.setJobClass("not.existing.Class");

    ExecutableService executableService = ExecutableService.getInstance();
    Throwable throwable = catchThrowable(() -> executableService.saveOrUpdate(executable));

    assertThat(throwable).isInstanceOf(NotExistingJobClassExecutableJobException.class);

  }

  @DisplayName("Can't delete an executable with running execution")
  @Test
  @Order(400)
  void testDeleteRunning() {
    Long id = ExecutableRepository.getInstance().findByClassName(ExecutableExample4.class.getName()).get(0).getId();

    ExecutableService executableService = ExecutableService.getInstance();

    Throwable throwable = catchThrowable(() -> executableService.deleteExecutable(id, true));

    assertThat(throwable).isInstanceOf(RemoveExecutableException.class).hasMessage(MESSAGE_RUNNING_EXECUTION);
  }

  @DisplayName("Soft delete : Only Executable without execution")
  @Test
  @Order(500)
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

  @DisplayName("Hard delete : Delete execution before executable")
  @Test
  @Order(600)
  void testDeleteHard() {
    Long id = ExecutableRepository.getInstance().findByClassName(ExecutableExample5.class.getName()).get(0).getId();

    ExecutableService executableService = ExecutableService.getInstance();

    executableService.deleteExecutable(id, true);

    Optional<Executable> executable = executableService.getById(id);
    assertThat(executable).isNotPresent();
  }
}