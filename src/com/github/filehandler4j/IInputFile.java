package com.github.filehandler4j;

import java.nio.file.Path;

public interface IInputFile {
  String getAbsolutePath();
  
  String getName();
  
  String getShortName();
  
  Path toPath();
  
  long length();
  
  boolean exists();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/filehandler4j/IInputFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */