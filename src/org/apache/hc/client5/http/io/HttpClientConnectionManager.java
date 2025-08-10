package org.apache.hc.client5.http.io;

import java.io.IOException;
import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.io.ModalCloseable;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

public interface HttpClientConnectionManager extends ModalCloseable {
  LeaseRequest lease(String paramString, HttpRoute paramHttpRoute, Timeout paramTimeout, Object paramObject);
  
  void release(ConnectionEndpoint paramConnectionEndpoint, Object paramObject, TimeValue paramTimeValue);
  
  void connect(ConnectionEndpoint paramConnectionEndpoint, TimeValue paramTimeValue, HttpContext paramHttpContext) throws IOException;
  
  void upgrade(ConnectionEndpoint paramConnectionEndpoint, HttpContext paramHttpContext) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/io/HttpClientConnectionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */