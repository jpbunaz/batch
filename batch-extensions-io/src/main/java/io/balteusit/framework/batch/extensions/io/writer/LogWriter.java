package io.balteusit.framework.batch.extensions.io.writer;

import io.balteusit.framework.batch.core.Env;
import io.balteusit.framework.batch.core.Writer;
import java.util.List;

public class LogWriter<T, E extends Env> implements Writer<T, E> {

  @Override
  public void write(List<T> writes, E env) {
    for (T write : writes) {
      env.getLogger().info(write.toString());
    }
  }

}
