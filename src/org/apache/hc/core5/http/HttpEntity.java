package org.apache.hc.core5.http;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.hc.core5.function.Supplier;

public interface HttpEntity extends EntityDetails, Closeable {
  boolean isRepeatable();
  
  InputStream getContent() throws IOException, UnsupportedOperationException;
  
  void writeTo(OutputStream paramOutputStream) throws IOException;
  
  boolean isStreaming();
  
  Supplier<List<? extends Header>> getTrailers();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/HttpEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */