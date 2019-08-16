package io.balteusit.framework.batch.execute.domain;

import java.util.Optional;

public interface ExecutableInterface {

  default boolean isAsynchronous() {
    return false;
  }

  default boolean isActive() {
    return true;
  }

  default Optional<String> getLog() {
    return Optional.empty();
  }

  ExecutionStatus perform();

}
