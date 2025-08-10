package org.apache.hc.core5.io;

import java.io.Closeable;

public interface ModalCloseable extends Closeable {
  void close(CloseMode paramCloseMode);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/io/ModalCloseable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */