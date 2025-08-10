package org.apache.hc.core5.http;

import java.io.IOException;
import java.net.SocketAddress;
import javax.net.ssl.SSLSession;

public interface HttpConnection extends SocketModalCloseable {
  void close() throws IOException;
  
  EndpointDetails getEndpointDetails();
  
  SocketAddress getLocalAddress();
  
  SocketAddress getRemoteAddress();
  
  ProtocolVersion getProtocolVersion();
  
  SSLSession getSSLSession();
  
  boolean isOpen();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/HttpConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */