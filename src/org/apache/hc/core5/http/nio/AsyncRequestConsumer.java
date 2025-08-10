package org.apache.hc.core5.http.nio;

import java.io.IOException;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.protocol.HttpContext;

public interface AsyncRequestConsumer<T> extends AsyncDataConsumer {
  void consumeRequest(HttpRequest paramHttpRequest, EntityDetails paramEntityDetails, HttpContext paramHttpContext, FutureCallback<T> paramFutureCallback) throws HttpException, IOException;
  
  void failed(Exception paramException);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/AsyncRequestConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */