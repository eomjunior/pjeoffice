package org.apache.hc.core5.http.nio;

import java.io.IOException;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.protocol.HttpContext;

public interface AsyncResponseConsumer<T> extends AsyncDataConsumer {
  void consumeResponse(HttpResponse paramHttpResponse, EntityDetails paramEntityDetails, HttpContext paramHttpContext, FutureCallback<T> paramFutureCallback) throws HttpException, IOException;
  
  void informationResponse(HttpResponse paramHttpResponse, HttpContext paramHttpContext) throws HttpException, IOException;
  
  void failed(Exception paramException);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/AsyncResponseConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */