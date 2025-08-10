package org.apache.tools.ant;

import java.io.PrintStream;

public interface BuildLogger extends BuildListener {
  void setMessageOutputLevel(int paramInt);
  
  void setOutputPrintStream(PrintStream paramPrintStream);
  
  void setEmacsMode(boolean paramBoolean);
  
  void setErrorPrintStream(PrintStream paramPrintStream);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/BuildLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */