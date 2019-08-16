package io.balteusit.framework.batch.core;

import static java.lang.String.format;

import io.balteusit.framework.batch.core.exception.MaximumSkipException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class ComposableProcess<E, T, G extends Env> implements Process<G> {

  private class EmptyTransformer<E, T, G extends Env> implements Transformer<E, T, G> {

    @Override
    @SuppressWarnings("unchecked")
    public T transform(E source, G env) {
      return (T) source;
    }

  }

  public static final String LOG_MAXIMUM_SKIP = "Process %s : error cause maximum skip is reached";

  public static final String LOG_SKIP_LINE = "Process %s skip line %s : %s";

  private int commitSize = 1000;

  private int maxSkip = 10;

  private Set<Class<? extends RuntimeException>> skipReadExceptions = new HashSet<>();

  private Set<Class<? extends RuntimeException>> skipFilterExceptions = new HashSet<>();

  private Set<Class<? extends RuntimeException>> skipTransformExceptions = new HashSet<>();

  private Set<Class<? extends RuntimeException>> skipWriteExceptions = new HashSet<>();

  private final String name;

  private int nbSkip = 0;

  private int currentLine = 0;

  private int processLine = 0;

  private Reader<E, G> reader;

  private List<Filter<E>> filters = new ArrayList<>();

  private Transformer<E, T, G> transformer = new EmptyTransformer<>();

  private Writer<T, G> writer;

  public ComposableProcess(String name) {
    this.name = name;
  }

  public ComposableProcess<E, T, G> withCommitSize(int commitSize) {
    this.commitSize = commitSize;
    return this;
  }

  public ComposableProcess<E, T, G> withMaxSkip(int maxSkip) {
    this.maxSkip = maxSkip;
    return this;
  }

  public ComposableProcess<E, T, G> withReader(Reader<E, G> reader) {
    this.reader = reader;
    return this;
  }

  public ComposableProcess<E, T, G> withFilter(Filter<E> filter) {
    this.filters.add(filter);
    return this;
  }

  public ComposableProcess<E, T, G> withFilters(List<Filter<E>> filters) {
    this.filters.addAll(filters);
    return this;
  }

  public ComposableProcess<E, T, G> withTransformer(Transformer<E, T, G> transformer) {
    this.transformer = transformer;
    return this;
  }

  public ComposableProcess<E, T, G> withWriter(Writer<T, G> writer) {
    this.writer = writer;
    return this;
  }

  @SafeVarargs
  public final ComposableProcess<E, T, G> withSkipReadExceptions(
      Class<? extends RuntimeException>... runtimeExceptions) {
    Collections.addAll(skipReadExceptions, runtimeExceptions);
    return this;
  }

  @SafeVarargs
  public final ComposableProcess<E, T, G> withSkipFilterExceptions(
      Class<? extends RuntimeException>... runtimeExceptions) {
    Collections.addAll(skipFilterExceptions, runtimeExceptions);
    return this;
  }

  @SafeVarargs
  public final ComposableProcess<E, T, G> withSkipTransformExceptions(
      Class<? extends RuntimeException>... runtimeExceptions) {
    Collections.addAll(skipTransformExceptions, runtimeExceptions);
    return this;
  }

  @SafeVarargs
  public final ComposableProcess<E, T, G> withSkipWriteExceptions(
      Class<? extends RuntimeException>... runtimeExceptions) {
    Collections.addAll(skipWriteExceptions, runtimeExceptions);
    return this;
  }

  @SafeVarargs
  public final ComposableProcess<E, T, G> withAllSkipWriteExceptions(
      Class<? extends RuntimeException>... runtimeExceptions) {
    return this.withSkipReadExceptions(runtimeExceptions)
        .withSkipFilterExceptions(runtimeExceptions)
        .withSkipTransformExceptions(runtimeExceptions)
        .withAllSkipWriteExceptions(runtimeExceptions);
  }

  public boolean start(G env) {
    try {
      List<T> elementsToWrite = new ArrayList<>();
      Optional<E> source;
      while ((source = executeRead(env)).isPresent()) {
        currentLine++;
        boolean isFiltered = executeFilter(source.get(), env);
        if (isFiltered) {
          processLine++;
          Optional<T> transformed = executeTransform(source.get(), env);
          if (transformed.isPresent()) {
            elementsToWrite.add(transformed.get());
            if (elementsToWrite.size() == commitSize) {
              executeWriter(elementsToWrite, env);
              elementsToWrite = new ArrayList<>();
            }
          }
        }
      }
      if (elementsToWrite.size() != 0) {
        executeWriter(elementsToWrite, env);
      }
      writer.afterProcess(env);
      return true;
    } catch (MaximumSkipException e) {
      env.getLogger().error(format(LOG_MAXIMUM_SKIP, name));
      throw e;
    }
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getNbOfSkipLine() {
    return nbSkip;
  }

  @Override
  public int getNbOfLine() {
    return currentLine;
  }

  @Override
  public int getNbOfLineProcess() {
    return processLine;
  }

  private Optional<E> executeRead(G jobEnv) {
    try {
      return reader.read(jobEnv);
    } catch (RuntimeException e) {
      if (isSkipException(e, skipReadExceptions)) {
        jobEnv.getLogger().warn(format(LOG_SKIP_LINE, name, currentLine, e.getMessage()));
        if (nbSkip < maxSkip) {
          nbSkip++;
          return executeRead(jobEnv);
        } else {
          throw new MaximumSkipException();
        }
      } else {
        throw e;
      }
    }
  }

  private boolean executeFilter(E source, Env jobEnv) {
    try {
      for (Filter<E> filter : filters) {
        boolean isFilter = filter.filter(source, jobEnv);
        if (!isFilter) {
          return false;
        }
      }

      return true;
    } catch (RuntimeException e) {
      if (isSkipException(e, skipFilterExceptions)) {
        jobEnv.getLogger().warn(format(LOG_SKIP_LINE, name, currentLine, e.getMessage()));
        if (nbSkip < maxSkip) {
          nbSkip++;
          return false;
        } else {
          throw new MaximumSkipException();
        }
      } else {
        throw e;
      }
    }
  }

  private Optional<T> executeTransform(E source, G jobEnv) {
    try {
      return Optional.of(transformer.transform(source, jobEnv));
    } catch (RuntimeException e) {
      if (isSkipException(e, skipTransformExceptions)) {
        jobEnv.getLogger().warn(format(LOG_SKIP_LINE, name, currentLine, e.getMessage()));
        if (nbSkip < maxSkip) {
          nbSkip++;
          return Optional.empty();
        } else {
          throw new MaximumSkipException();
        }
      } else {
        throw e;
      }
    }
  }

  private void executeWriter(List<T> writes, G jobEnv) {
    boolean success = false;
    try {
      writer.write(writes, jobEnv);
      success = true;
    } catch (RuntimeException e) {
      if (isSkipException(e, skipWriteExceptions)) {
        jobEnv.getLogger().warn(format(LOG_SKIP_LINE, name, currentLine, e.getMessage()));
        if (nbSkip < maxSkip) {
          nbSkip = nbSkip + writes.size();
        } else {
          throw new MaximumSkipException();
        }
      } else {
        throw e;
      }
    }
  }

  private boolean isSkipException(RuntimeException e,
      Set<Class<? extends RuntimeException>> skipExceptions) {
    for (Class<? extends RuntimeException> skipException : skipExceptions) {
      if (skipException.isInstance(e)) {
        return true;
      }
    }
    return false;
  }

}
