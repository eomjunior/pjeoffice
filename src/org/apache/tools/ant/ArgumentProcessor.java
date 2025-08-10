package org.apache.tools.ant;

import java.io.PrintStream;
import java.util.List;

public interface ArgumentProcessor {
  int readArguments(String[] paramArrayOfString, int paramInt);
  
  boolean handleArg(List<String> paramList);
  
  void prepareConfigure(Project paramProject, List<String> paramList);
  
  boolean handleArg(Project paramProject, List<String> paramList);
  
  void printUsage(PrintStream paramPrintStream);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/ArgumentProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */