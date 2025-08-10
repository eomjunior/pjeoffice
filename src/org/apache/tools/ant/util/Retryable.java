package org.apache.tools.ant.util;

import java.io.IOException;

public interface Retryable {
  public static final int RETRY_FOREVER = -1;
  
  void execute() throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/Retryable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */