package org.apache.hc.core5.http.io;

import java.io.IOException;
import org.apache.hc.core5.http.HttpConnection;
import org.apache.hc.core5.util.Timeout;

public interface BHttpConnection extends HttpConnection {
  boolean isDataAvailable(Timeout paramTimeout) throws IOException;
  
  boolean isStale() throws IOException;
  
  void flush() throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/BHttpConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */