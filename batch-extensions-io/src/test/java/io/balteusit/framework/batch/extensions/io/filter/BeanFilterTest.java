package io.balteusit.framework.batch.extensions.io.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import io.balteusit.framework.batch.core.Env;
import java.util.Date;
import java.util.HashMap;
import org.junit.Test;

public class BeanFilterTest {

  private class Cat {

    private String name;

    private boolean male;

    private Date birthDate;

    public Cat(String name, boolean male, Date birthDate) {
      this.name = name;
      this.male = male;
      this.birthDate = birthDate;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public boolean isMale() {
      return male;
    }

    public void setMale(boolean male) {
      this.male = male;
    }

    public Date getBirthDate() {
      return birthDate;
    }

    public void setBirthDate(Date birthDate) {
      this.birthDate = birthDate;
    }
  }

  private Cat gizmo = new Cat("Gizmo", false, new Date());
  private Cat crob = new Cat("Crob", true, new Date());
  private Cat laChose = new Cat("La chose", false, new Date());

  @Test
  public void filter() {
    BeanFilter<Cat> catBeanFilter = new BeanFilter<Cat>().property("name").isEqualTo("Gizmo", "La chose");
    checkFilter(catBeanFilter, gizmo, true);
    checkFilter(catBeanFilter, crob, false);
    checkFilter(catBeanFilter, laChose, true);
  }

  @Test
  public void filterBoolean() {
    BeanFilter<Cat> catBeanFilter = new BeanFilter<Cat>().property("male").isEqualTo(true);
    checkFilter(catBeanFilter, gizmo, false);
    checkFilter(catBeanFilter, crob, true);
    checkFilter(catBeanFilter, laChose, false);
  }

  @Test
  public void filterNoProperty() {
    BeanFilter<Cat> catBeanFilter = new BeanFilter<Cat>();
    checkFail(catBeanFilter);
  }

  @Test
  public void filterMethodNotExist() {
    BeanFilter<Cat> catBeanFilter = new BeanFilter<Cat>().property("apropertywhichnotexist");
    checkFail(catBeanFilter);
  }

  @Test
  public void filterNoValue() {
    BeanFilter<Cat> catBeanFilter = new BeanFilter<Cat>().property("male");
    checkFail(catBeanFilter);
  }

  private void checkFail(BeanFilter<Cat> catBeanFilter) {
    try {
      checkFilter(catBeanFilter, gizmo, false);
      fail("Should throw an exception");
    } catch (RuntimeException e) {
      return;
    }
    fail("Should throw an exception");
  }



  private void checkFilter(BeanFilter<Cat> catBeanFilter, Cat cat, boolean expected) {
    Env env = new Env(new HashMap<>(), "test");
    boolean filter = catBeanFilter.filter(cat, env);
    assertThat(filter).isEqualTo(expected);
  }


}