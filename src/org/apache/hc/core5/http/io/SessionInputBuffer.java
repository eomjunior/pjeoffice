package org.apache.hc.core5.http.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.hc.core5.util.CharArrayBuffer;

public interface SessionInputBuffer {
  int length();
  
  int capacity();
  
  int available();
  
  int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, InputStream paramInputStream) throws IOException;
  
  int read(byte[] paramArrayOfbyte, InputStream paramInputStream) throws IOException;
  
  int read(InputStream paramInputStream) throws IOException;
  
  int readLine(CharArrayBuffer paramCharArrayBuffer, InputStream paramInputStream) throws IOException;
  
  HttpTransportMetrics getMetrics();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/SessionInputBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */