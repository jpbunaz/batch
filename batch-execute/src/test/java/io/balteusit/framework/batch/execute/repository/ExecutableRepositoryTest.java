package io.balteusit.framework.batch.execute.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.balteusit.framework.batch.execute.common.AbstractDatabaseTest;
import io.balteusit.framework.batch.execute.domain.Executable;
import io.balteusit.framework.batch.execute.service.ExecutableExample6;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class ExecutableRepositoryTest extends AbstractDatabaseTest {

  private static final String CLASSNAME = ExecutableExample6.class.getName();

  private static final String NAME = "ExecutableExample6";

  private static final boolean ASYNCHRONOUS = true;

  private static final boolean ACTIVE = false;

  private final ExecutableRepository executableRepository = ExecutableRepository.getInstance();


  @DisplayName("Read an existing executable by this ID")
  @Test
  @Order(1)
  public void testGetById() {

    Long id = executableRepository.findByClassName(CLASSNAME).get(0).getId();

    Optional<Executable> executableFromBDDOpt = executableRepository.getById(id);

    assertThat(executableFromBDDOpt).isNotEmpty();
    assertThat(executableFromBDDOpt.get().getClassName()).isEqualTo(CLASSNAME);
    assertThat(executableFromBDDOpt.get().getName()).isEqualTo(NAME);
    assertThat(executableFromBDDOpt.get().isAsynchronous()).isEqualTo(ASYNCHRONOUS);
    assertThat(executableFromBDDOpt.get().isActive()).isEqualTo(ACTIVE);
  }

  @DisplayName("Create an executable")
  @Test
  @Order(2)
  public void testPersistForCreate() {
    Executable newExecutable = new Executable();
    String newClassname = CLASSNAME + "new";
    newExecutable.setClassName(newClassname);
    newExecutable.setAsynchronous(ASYNCHRONOUS);
    String newName = NAME + "new";
    newExecutable.setName(newName);

    executableRepository.persistNow(newExecutable);

    Optional<Executable> executableFromBddOpt = executableRepository.getById(newExecutable.getId());

    assertThat(executableFromBddOpt).isPresent().containsInstanceOf(Executable.class).get().isEqualToComparingFieldByField(newExecutable);
  }

  @DisplayName("Update an executable")
  @Test
  @Order(3)
  public void testPersistForUpdate() {
    Long id = executableRepository.findByClassName(CLASSNAME).get(0).getId();

    Executable updateExecutable = executableRepository.getById(id).orElseThrow(RuntimeException::new);
    updateExecutable.setAsynchronous(ASYNCHRONOUS);
    String newName = NAME + "updated";
    updateExecutable.setName(newName);

    executableRepository.persistNow(updateExecutable);

    Optional<Executable> executableFromBddOpt = executableRepository.getById(id);

    assertThat(executableFromBddOpt).isPresent().containsInstanceOf(Executable.class).get()
        .isEqualToComparingFieldByField(updateExecutable);
  }

  @DisplayName("Remove an existing executable")
  @Test
  @Order(4)
  public void testRemove() {
    Long id = executableRepository.findByClassName(CLASSNAME + "new").get(0).getId();
    executableRepository.removeNow(id);

    Optional<Executable> executableFromBddOpt = executableRepository.getById(id);
    assertThat(executableFromBddOpt).isEmpty();
  }


}