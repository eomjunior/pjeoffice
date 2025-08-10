package org.apache.hc.core5.http.io;

import java.io.IOException;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;

public interface HttpServerConnection extends BHttpConnection {
  ClassicHttpRequest receiveRequestHeader() throws HttpException, IOException;
  
  void receiveRequestEntity(ClassicHttpRequest paramClassicHttpRequest) throws HttpException, IOException;
  
  void sendResponseHeader(ClassicHttpResponse paramClassicHttpResponse) throws HttpException, IOException;
  
  void sendResponseEntity(ClassicHttpResponse paramClassicHttpResponse) throws HttpException, IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/HttpServerConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */