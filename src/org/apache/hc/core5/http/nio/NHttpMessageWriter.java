package org.apache.hc.core5.http.nio;

import java.io.IOException;
import org.apache.hc.core5.http.HttpException;

public interface NHttpMessageWriter<T extends org.apache.hc.core5.http.MessageHeaders> {
  void reset();
  
  void write(T paramT, SessionOutputBuffer paramSessionOutputBuffer) throws IOException, HttpException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/NHttpMessageWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */