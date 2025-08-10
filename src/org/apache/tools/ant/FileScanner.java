package org.apache.tools.ant;

import java.io.File;

public interface FileScanner {
  void addDefaultExcludes();
  
  File getBasedir();
  
  String[] getExcludedDirectories();
  
  String[] getExcludedFiles();
  
  String[] getIncludedDirectories();
  
  String[] getIncludedFiles();
  
  String[] getNotIncludedDirectories();
  
  String[] getNotIncludedFiles();
  
  void scan() throws IllegalStateException;
  
  void setBasedir(String paramString);
  
  void setBasedir(File paramFile);
  
  void setExcludes(String[] paramArrayOfString);
  
  void setIncludes(String[] paramArrayOfString);
  
  void setCaseSensitive(boolean paramBoolean);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/FileScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */