package org.apache.hc.core5.http.nio;

import java.io.IOException;
import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.SAFE)
public interface ResponseChannel {
  void sendInformation(HttpResponse paramHttpResponse, HttpContext paramHttpContext) throws HttpException, IOException;
  
  void sendResponse(HttpResponse paramHttpResponse, EntityDetails paramEntityDetails, HttpContext paramHttpContext) throws HttpException, IOException;
  
  void pushPromise(HttpRequest paramHttpRequest, AsyncPushProducer paramAsyncPushProducer, HttpContext paramHttpContext) throws HttpException, IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/ResponseChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */