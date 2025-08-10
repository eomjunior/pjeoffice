package org.apache.hc.core5.http.nio;

import java.io.IOException;
import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.STATELESS)
public interface AsyncServerRequestHandler<T> {
  AsyncRequestConsumer<T> prepare(HttpRequest paramHttpRequest, EntityDetails paramEntityDetails, HttpContext paramHttpContext) throws HttpException;
  
  void handle(T paramT, ResponseTrigger paramResponseTrigger, HttpContext paramHttpContext) throws HttpException, IOException;
  
  public static interface ResponseTrigger {
    void sendInformation(HttpResponse param1HttpResponse, HttpContext param1HttpContext) throws HttpException, IOException;
    
    void submitResponse(AsyncResponseProducer param1AsyncResponseProducer, HttpContext param1HttpContext) throws HttpException, IOException;
    
    void pushPromise(HttpRequest param1HttpRequest, HttpContext param1HttpContext, AsyncPushProducer param1AsyncPushProducer) throws HttpException, IOException;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/AsyncServerRequestHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */