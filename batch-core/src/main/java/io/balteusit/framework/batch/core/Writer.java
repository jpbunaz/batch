package io.balteusit.framework.batch.core;

import java.util.List;

public interface Writer<T, E extends Env> {

  void write(List<T> writes, E env);

  default void afterProcess(E env) {

  }

}
