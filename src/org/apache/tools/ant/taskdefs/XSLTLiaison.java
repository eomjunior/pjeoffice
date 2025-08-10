package org.apache.tools.ant.taskdefs;

import java.io.File;

public interface XSLTLiaison {
  public static final String FILE_PROTOCOL_PREFIX = "file://";
  
  void setStylesheet(File paramFile) throws Exception;
  
  void addParam(String paramString1, String paramString2) throws Exception;
  
  void transform(File paramFile1, File paramFile2) throws Exception;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/XSLTLiaison.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */