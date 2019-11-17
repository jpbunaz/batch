package io.balteusit.framework.batch.execute.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import io.balteusit.framework.batch.execute.common.AbstractDatabaseTest;
import io.balteusit.framework.batch.execute.domain.ExecutableJob;
import io.balteusit.framework.batch.execute.domain.ExecutionStatus;
import io.balteusit.framework.batch.execute.repository.ExecutableRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class LaunchServiceTest extends AbstractDatabaseTest {

  @DisplayName("Launch an executable sync")
  @Test
  @Order(100)
  void launch() throws SQLException {

    Long executableId = getExecutableId(ExecutableExample1.class.getName());

    LaunchService launchService = LaunchService.getInstance();

    ExecutionStatus status = launchService.launch(executableId);

    assertThat(status).isEqualTo(ExecutionStatus.SUCCESS);

    checkExecutionFromExecutable(executableId);

  }

  @DisplayName("Launch an executable async")
  @Test
  @Order(200)
  void launchAsync() throws SQLException, ExecutionException, InterruptedException {
    Long executableId = getExecutableId(ExecutableExample2.class.getName());

    LaunchService launchService = LaunchService.getInstance();

    Future<ExecutionStatus> statusFuture = launchService.launchAsync(executableId);

    ExecutionStatus executionStatus = statusFuture.get();

    assertThat(executionStatus).isEqualTo(ExecutionStatus.FAILED);

    checkExecutionFromExecutable(executableId);

  }

  @DisplayName("Launch a executable job")
  @Test
  @Order(300)
  void launchExecutableJob() throws SQLException {
    Long executableId = getExecutableId(ExecutableJob.class.getName());

    LaunchService launchService = LaunchService.getInstance();

    ExecutionStatus executionStatus = launchService.launch(executableId);

    assertThat(executionStatus).isEqualTo(ExecutionStatus.SUCCESS);

    checkExecutionFromExecutable(executableId);
  }

  private void checkExecutionFromExecutable(Long id) throws SQLException {
    try (Connection connection = getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from execution where executable_id = ?");
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (!resultSet.next()) {
        fail("Should have an execution");
      }
    }
  }

  private Long getExecutableId(String className) {
    ExecutableRepository executableRepository = ExecutableRepository.getInstance();
    return executableRepository.findByClassName(className).get(0).getId();
  }
}