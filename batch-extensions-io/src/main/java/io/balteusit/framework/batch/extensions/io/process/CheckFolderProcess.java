package io.balteusit.framework.batch.extensions.io.process;

import static java.lang.String.format;

import io.balteusit.framework.batch.core.Env;
import io.balteusit.framework.batch.core.Process;
import java.io.File;

public class CheckFolderProcess implements Process {

  private String folderPath;

  public CheckFolderProcess(String folderPath) {
    this.folderPath = folderPath;
  }

  @Override
  public boolean start(Env env) {
    File inputFileFolder = new File(folderPath);
    if (inputFileFolder.exists() && inputFileFolder.isDirectory() && inputFileFolder.canWrite()) {
      return true;
    } else {
      throw new RuntimeException(format("Source folder %s is not a valid. It should be a folder with write right", folderPath));
    }
  }

  @Override
  public String getName() {
    return "Check folder";
  }

}
