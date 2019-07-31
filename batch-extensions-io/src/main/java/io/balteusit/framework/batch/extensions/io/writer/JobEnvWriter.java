package io.balteusit.framework.batch.extensions.io.writer;

import io.balteusit.framework.batch.core.Env;
import io.balteusit.framework.batch.core.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JobEnvWriter<T, E extends Env> implements Writer<T, E> {

  private String key;

  public JobEnvWriter(String key) {
    this.key = key;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void write(List<T> writes, E env) {

    Optional<Object> objectInSession = env.getSession(key);

    List<T> current = new ArrayList<>();

    if (objectInSession.isPresent()) {
      Object object = objectInSession.get();
      if (object instanceof List) {
        current = (List<T>)objectInSession.get();
      }
    }
    current.addAll(writes);

    env.setSession(key, current);
  }

}
