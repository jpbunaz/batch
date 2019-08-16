package io.balteusit.framework.batch.core;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Job {

  private static final String LOG_PROCESS_SUCCESS = "Process %s : Success - %s process on %s - %s";

  private static final String LOG_PROCESS_START = "Process %s : start";

  private static final String LOG_PROCESS_WARNING_SKIP = "Process %s : warning, %s process on %s, %s are skip - %s";

  private static final String LOG_JOB_START = "Job %s : Start";

  private static final String LOG_JOB_SUCESS = "Job %s : End - Success";

  private static final String LOG_JOB_ERROR = "Job %s : Error cause : %s";

  private List<Process> processes = new ArrayList<>();

  private final String name;

  private final Env env;

  private Process failedCallBack;

  public Job(String name) {
    this.name = name;
    this.env = new Env();
    this.env.setLogger(LoggerFactory.getLogger(name));
  }

  public Job addProcess(Process... process) {
    processes.addAll(Arrays.asList(process));
    return this;
  }

  public Job withFailedCallback(Process wfjFailedCallBack) {
    this.failedCallBack = wfjFailedCallBack;
    return this;
  }

  @SuppressWarnings("unchecked")
  public void start() {
    Logger logger = env.getLogger();
    logger.info(format(LOG_JOB_START, name));
    boolean toContinue = true;
    try {
      for (Process process : processes) {
        if (toContinue) {
          logger.info(format(LOG_PROCESS_START, process.getName()));
          long start = System.currentTimeMillis();
          toContinue = process.start(env);
          long end = System.currentTimeMillis();
          long time = end - start;
          if (process.getNbOfSkipLine() == 0) {
            logger.info(format(LOG_PROCESS_SUCCESS, process.getName(), process.getNbOfLineProcess(),
                process.getNbOfLine(), time));
          } else {
            logger.info(
                format(LOG_PROCESS_WARNING_SKIP, process.getName(), process.getNbOfLineProcess(),
                    process.getNbOfLine(),
                    process.getNbOfSkipLine(), time));
          }
        }
      }
      logger.info(format(LOG_JOB_SUCESS, name));
    } catch (Exception e) {
      if (failedCallBack != null) {
        failedCallBack.start(env);
      }
      logger.error(format(LOG_JOB_ERROR, name, e.getMessage()), e);
      throw e;
    }
  }

}
