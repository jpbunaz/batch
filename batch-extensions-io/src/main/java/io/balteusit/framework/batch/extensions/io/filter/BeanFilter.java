package io.balteusit.framework.batch.extensions.io.filter;

import static io.balteusit.framework.batch.core.util.StringUtils.isNullOrEmpty;

import io.balteusit.framework.batch.core.Env;
import io.balteusit.framework.batch.core.Filter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanFilter<T> implements Filter<T> {

  private String property;

  private Object[] values = new Object[0];

  private Boolean booleanValueSpecialCase;

  public BeanFilter<T> property(String property) {
    this.property = property;
    return this;
  }

  public BeanFilter<T> isEqualTo(Object... values) {
    this.values = values;
    return this;
  }

  public BeanFilter<T> isEqualTo(boolean zBoolean) {
    this.booleanValueSpecialCase = zBoolean;
    return this;
  }

  @Override
  public boolean filter(T source, Env env) {
    try {

      if (values == null) {
        throw new RuntimeException("values can't be null or empty");
      }

      String methodName = getMethodName();
      Method method = source.getClass().getMethod(methodName);
      Object invoke = method.invoke(source);
      if (booleanValueSpecialCase != null) {
        if (invoke.equals(booleanValueSpecialCase)) {
          return true;
        }
      } else {
        for (Object value : values) {
          if (invoke.equals(value)) {
            return true;
          }
        }
      }
      return false;
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Error during filter", e);
    }

  }

  private String getMethodName() {
    if (isNullOrEmpty(property)) {
      throw new RuntimeException("property can't be null or empty");
    }
    if (booleanValueSpecialCase == null && values.length == 0) {
      throw new RuntimeException("value can't be null or empty");
    }
    if (booleanValueSpecialCase != null) {
      return "is" + property.substring(0, 1).toUpperCase() + property.substring(1);
    }
    return "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
  }
}
