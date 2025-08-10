package org.apache.hc.client5.http.async;

import java.io.IOException;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.nio.AsyncDataConsumer;

public interface AsyncExecCallback {
  AsyncDataConsumer handleResponse(HttpResponse paramHttpResponse, EntityDetails paramEntityDetails) throws HttpException, IOException;
  
  void handleInformationResponse(HttpResponse paramHttpResponse) throws HttpException, IOException;
  
  void completed();
  
  void failed(Exception paramException);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/AsyncExecCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */