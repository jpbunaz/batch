package io.balteusit.framework.batch.execute.service;

import io.balteusit.framework.batch.execute.domain.ExecutableInterface;
import io.balteusit.framework.batch.execute.domain.ExecutionStatus;

public class ExecutableExample3 implements ExecutableInterface {

  @Override
  public ExecutionStatus perform() {
    return ExecutionStatus.SUCCESS;
  }

}
