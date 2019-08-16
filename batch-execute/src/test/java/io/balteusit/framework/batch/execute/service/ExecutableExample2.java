package io.balteusit.framework.batch.execute.service;

import io.balteusit.framework.batch.execute.domain.ExecutableInterface;
import io.balteusit.framework.batch.execute.domain.ExecutionStatus;

public class ExecutableExample2 implements ExecutableInterface {

  @Override
  public ExecutionStatus perform() {

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    return ExecutionStatus.FAILED;
  }

  @Override
  public boolean isAsynchronous() {
    return true;
  }

  @Override
  public boolean isActive() {
    return false;
  }
}
