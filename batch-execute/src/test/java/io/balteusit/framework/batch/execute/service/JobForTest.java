package io.balteusit.framework.batch.execute.service;

import io.balteusit.framework.batch.core.Env;
import io.balteusit.framework.batch.core.Job;
import io.balteusit.framework.batch.core.Process;

public class JobForTest extends Job {

  public JobForTest() {
    super("Job for test");
    this.addProcess(new Process() {
      @Override
      public boolean start(Env env) {
        env.getLogger().error("I write something");
        return false;
      }
    });
  }

}
