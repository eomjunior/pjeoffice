package org.apache.hc.core5.http2.impl.nio;

import java.io.IOException;
import java.util.List;
import org.apache.hc.core5.concurrent.Cancellable;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.nio.AsyncPushProducer;
import org.apache.hc.core5.http.nio.CapacityChannel;
import org.apache.hc.core5.http.nio.DataStreamChannel;

interface H2StreamChannel extends DataStreamChannel, CapacityChannel, Cancellable {
  void submit(List<Header> paramList, boolean paramBoolean) throws HttpException, IOException;
  
  void push(List<Header> paramList, AsyncPushProducer paramAsyncPushProducer) throws HttpException, IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/H2StreamChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */