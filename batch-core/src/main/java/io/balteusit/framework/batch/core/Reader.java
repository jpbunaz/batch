package io.balteusit.framework.batch.core;

import java.util.Optional;

public interface Reader<G, E extends Env> {

  Optional<G> read(E env);

}
