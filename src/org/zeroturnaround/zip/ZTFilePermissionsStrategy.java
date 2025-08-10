package org.zeroturnaround.zip;

import java.io.File;

public interface ZTFilePermissionsStrategy {
  ZTFilePermissions getPermissions(File paramFile);
  
  void setPermissions(File paramFile, ZTFilePermissions paramZTFilePermissions);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/ZTFilePermissionsStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */