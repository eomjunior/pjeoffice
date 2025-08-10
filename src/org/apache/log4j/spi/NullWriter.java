package org.apache.log4j.spi;

import java.io.Writer;

class NullWriter extends Writer {
  public void close() {}
  
  public void flush() {}
  
  public void write(char[] cbuf, int off, int len) {}
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/spi/NullWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */