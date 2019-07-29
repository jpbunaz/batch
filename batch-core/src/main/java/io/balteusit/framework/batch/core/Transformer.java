package io.balteusit.framework.batch.core;

public interface Transformer<I, O, E extends Env> {

  O transform(I source, E env);

}
