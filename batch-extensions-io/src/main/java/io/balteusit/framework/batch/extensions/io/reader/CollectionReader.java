package io.balteusit.framework.batch.extensions.io.reader;

import io.balteusit.framework.batch.core.Env;
import io.balteusit.framework.batch.core.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

public class CollectionReader<T, E extends Env> implements Reader<T, E> {

  private Iterator<T> iterator;
  private Class<T> zClass;

  public CollectionReader(Class<T> zClass) {
    this.zClass = zClass;
  }

  public CollectionReader(Collection<T> collection) {
    this.iterator = collection.iterator();
  }

  @Override
  public Optional<T> read(E env) {
    if (iterator == null) {
      this.iterator = env.getCollection(zClass).iterator();
    }
    if (iterator.hasNext()) {
      return Optional.of(iterator.next());
    }
    return Optional.empty();
  }

}
