package io.balteusit.framework.batch.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class StringUtilsTest {

  @Test
  public void random() {
    String random = StringUtils.random(10);
    assertThat(random).hasSize(10).matches("[a-z]{10}");
  }

}