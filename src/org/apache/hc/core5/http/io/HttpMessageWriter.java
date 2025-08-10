package org.apache.hc.core5.http.io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.hc.core5.http.HttpException;

public interface HttpMessageWriter<T extends org.apache.hc.core5.http.MessageHeaders> {
  void write(T paramT, SessionOutputBuffer paramSessionOutputBuffer, OutputStream paramOutputStream) throws IOException, HttpException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/HttpMessageWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */