package org.apache.hc.core5.io;

import java.io.IOException;

public interface IOCallback<T> {
  void execute(T paramT) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/io/IOCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */