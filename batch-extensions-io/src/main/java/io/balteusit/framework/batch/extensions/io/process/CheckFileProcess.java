package io.balteusit.framework.batch.extensions.io.process;


import io.balteusit.framework.batch.core.Env;
import io.balteusit.framework.batch.core.Process;
import java.io.File;

public class CheckFileProcess implements Process {

  private String filePath;

  public CheckFileProcess(String filePath) {
    this.filePath = filePath;
  }

  @Override
  public boolean start(Env env) {
    File inputFileFolder = new File(filePath);
    if (inputFileFolder.exists() && inputFileFolder.isFile() && inputFileFolder.canWrite()) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public String getName() {
    return "Check folder";
  }

}
