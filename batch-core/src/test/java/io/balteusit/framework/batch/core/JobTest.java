package io.balteusit.framework.batch.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;

public class JobTest {

  @Test
  public void start() {
    List<String> checkingList = new ArrayList<>();
    Job job = new Job("Job name");
    job.addProcess(env -> checkingList.add("Process 1"));
    job.addProcess(env -> checkingList.add("Process 2"));
    job.addProcess(env -> {
      checkingList.remove(0);
      return true;
    });
    job.addProcess(env -> checkingList.add("Process 3"));
    job.addProcess(env -> checkingList.add("Process 4"));

    assertThat(checkingList).isEmpty();

    job.start(new Env(new HashMap<>(), Job.class));

    assertThat(checkingList).isNotEmpty().containsExactlyInAnyOrder("Process 2", "Process 3", "Process 4");

  }

}