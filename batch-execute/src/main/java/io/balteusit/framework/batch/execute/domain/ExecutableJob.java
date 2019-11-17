package io.balteusit.framework.batch.execute.domain;

import io.balteusit.framework.batch.core.Job;

public class ExecutableJob  implements ExecutableInterface {

  private Job job;

  public ExecutableJob() {
  }

  public ExecutableJob(Job job) {
    this.job = job;
  }

  @Override
  public boolean isAutoExecutable() {
    return false;
  }

  @Override
  public ExecutionStatus perform() {
    job.start();
    return ExecutionStatus.SUCCESS;
  }

}
