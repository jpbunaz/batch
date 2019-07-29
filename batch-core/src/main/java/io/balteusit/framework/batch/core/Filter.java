package io.balteusit.framework.batch.core;

public interface Filter<E> {

  boolean filter(E source, Env env);

}
