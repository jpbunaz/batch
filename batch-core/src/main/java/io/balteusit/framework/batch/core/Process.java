package io.balteusit.framework.batch.core;

import static io.balteusit.framework.batch.core.util.StringUtils.random;

public interface Process<T extends Env> {

  boolean start(T env);

  default String getName() {
    return "process-" + random(10);
  }

  default int getNbOfSkipLine() {
    return 0;
  }

  default int getNbOfLine() {
    return 0;
  }

  default int getNbOfLineProcess() {
    return 0;
  }

}
