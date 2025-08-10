package com.itextpdf.text.io;

import java.io.IOException;

public interface RandomAccessSource {
  int get(long paramLong) throws IOException;
  
  int get(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
  
  long length();
  
  void close() throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/io/RandomAccessSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */