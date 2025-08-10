package org.apache.hc.client5.http.io;

import java.io.IOException;
import java.net.Socket;
import javax.net.ssl.SSLSession;
import org.apache.hc.core5.annotation.Internal;
import org.apache.hc.core5.http.io.HttpClientConnection;

@Internal
public interface ManagedHttpClientConnection extends HttpClientConnection {
  void bind(Socket paramSocket) throws IOException;
  
  Socket getSocket();
  
  SSLSession getSSLSession();
  
  void passivate();
  
  void activate();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/io/ManagedHttpClientConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */