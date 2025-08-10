package org.apache.hc.core5.http.io;

import java.io.IOException;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;

public interface HttpClientConnection extends BHttpConnection {
  boolean isConsistent();
  
  void sendRequestHeader(ClassicHttpRequest paramClassicHttpRequest) throws HttpException, IOException;
  
  void terminateRequest(ClassicHttpRequest paramClassicHttpRequest) throws HttpException, IOException;
  
  void sendRequestEntity(ClassicHttpRequest paramClassicHttpRequest) throws HttpException, IOException;
  
  ClassicHttpResponse receiveResponseHeader() throws HttpException, IOException;
  
  void receiveResponseEntity(ClassicHttpResponse paramClassicHttpResponse) throws HttpException, IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/HttpClientConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */