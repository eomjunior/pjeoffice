package org.apache.hc.client5.http.async;

import java.util.concurrent.Future;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.nio.AsyncPushConsumer;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.http.nio.HandlerFactory;
import org.apache.hc.core5.http.protocol.HttpContext;

public interface HttpAsyncClient {
  <T> Future<T> execute(AsyncRequestProducer paramAsyncRequestProducer, AsyncResponseConsumer<T> paramAsyncResponseConsumer, HandlerFactory<AsyncPushConsumer> paramHandlerFactory, HttpContext paramHttpContext, FutureCallback<T> paramFutureCallback);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/HttpAsyncClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */