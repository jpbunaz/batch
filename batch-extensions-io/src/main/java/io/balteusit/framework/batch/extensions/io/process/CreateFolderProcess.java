package io.balteusit.framework.batch.extensions.io.process;

import io.balteusit.framework.batch.core.Env;
import io.balteusit.framework.batch.core.Process;
import java.io.File;

public class CreateFolderProcess implements Process {

  private String folderPath;

  public CreateFolderProcess(String folderPath) {
    this.folderPath = folderPath;
  }

  @Override
  public boolean start(Env env) {

    File folderToCreate = new File(folderPath);

    if (folderToCreate.exists() && folderToCreate.isDirectory()) {
      return true;
    }

    boolean mkdirs = folderToCreate.mkdirs();

    if (mkdirs) {
      return true;
    } else {
      throw new RuntimeException(String.format("Can't create folder %s", folderPath));
    }

  }

  @Override
  public String getName() {
    return "Create folder";
  }

}
