package io.balteusit.framework.batch.execute.repository;

public class Order {

  private String field;

  private boolean asc;

  public Order(String field, boolean asc) {
    this.field = field;
    this.asc = asc;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public boolean isAsc() {
    return asc;
  }

  public void setAsc(boolean asc) {
    this.asc = asc;
  }
}
