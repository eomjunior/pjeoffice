package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ExecuteStreamHandler {
  void setProcessInputStream(OutputStream paramOutputStream) throws IOException;
  
  void setProcessErrorStream(InputStream paramInputStream) throws IOException;
  
  void setProcessOutputStream(InputStream paramInputStream) throws IOException;
  
  void start() throws IOException;
  
  void stop();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/ExecuteStreamHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */