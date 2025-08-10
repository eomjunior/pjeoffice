package org.apache.hc.core5.http.nio.support.classic;

import java.io.IOException;

public interface ContentOutputBuffer {
  int length();
  
  void reset();
  
  void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
  
  void write(int paramInt) throws IOException;
  
  void writeCompleted() throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/classic/ContentOutputBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */