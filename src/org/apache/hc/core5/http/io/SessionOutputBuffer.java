package org.apache.hc.core5.http.io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.hc.core5.util.CharArrayBuffer;

public interface SessionOutputBuffer {
  int length();
  
  int capacity();
  
  int available();
  
  void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, OutputStream paramOutputStream) throws IOException;
  
  void write(byte[] paramArrayOfbyte, OutputStream paramOutputStream) throws IOException;
  
  void write(int paramInt, OutputStream paramOutputStream) throws IOException;
  
  void writeLine(CharArrayBuffer paramCharArrayBuffer, OutputStream paramOutputStream) throws IOException;
  
  void flush(OutputStream paramOutputStream) throws IOException;
  
  HttpTransportMetrics getMetrics();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/SessionOutputBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */