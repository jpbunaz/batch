package io.balteusit.framework.batch.execute.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Executable {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  private String className;

  private String name;

  private boolean asynchronous;

  private boolean active;

  public Executable() {
  }

  public Executable(Long id, String className, String name, boolean asynchronous, boolean active) {
    this.id = id;
    this.className = className;
    this.name = name;
    this.asynchronous = asynchronous;
    this.active = active;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String id) {
    this.className = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isAsynchronous() {
    return asynchronous;
  }

  public void setAsynchronous(boolean asynchronous) {
    this.asynchronous = asynchronous;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
