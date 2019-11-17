package io.balteusit.framework.batch.execute.service;

import io.balteusit.framework.batch.execute.domain.ExecutableInterface;
import io.balteusit.framework.batch.execute.domain.ExecutionStatus;

public class ExecutableExample7 implements ExecutableInterface {

  @Override
  public boolean isAutoExecutable() {
    return false;
  }

  @Override
  public ExecutionStatus perform() {
    return ExecutionStatus.SUCCESS;
  }

}
