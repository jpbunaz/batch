package io.balteusit.framework.batch.execute.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Execution {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  private Date startDate;

  private Date endDate;

  @Enumerated(EnumType.STRING)
  private ExecutionStatus status;

  @ManyToOne
  private Executable executable;

  @Lob
  private String log;

  public Execution() {
  }

  public Execution(Date startDate, Date endDate, ExecutionStatus status, Executable executable, String log) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.status = status;
    this.executable = executable;
    this.log = log;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public ExecutionStatus getStatus() {
    return status;
  }

  public void setStatus(ExecutionStatus status) {
    this.status = status;
  }

  public Executable getExecutable() {
    return executable;
  }

  public void setExecutable(Executable executable) {
    this.executable = executable;
  }

  public String getLog() {
    return log;
  }

  public void setLog(String log) {
    this.log = log;
  }
}
