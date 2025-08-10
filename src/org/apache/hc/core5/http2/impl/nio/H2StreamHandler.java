package org.apache.hc.core5.http2.impl.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.nio.AsyncPushConsumer;
import org.apache.hc.core5.http.nio.HandlerFactory;
import org.apache.hc.core5.http.nio.ResourceHolder;

interface H2StreamHandler extends ResourceHolder {
  boolean isOutputReady();
  
  void produceOutput() throws HttpException, IOException;
  
  void consumePromise(List<Header> paramList) throws HttpException, IOException;
  
  void consumeHeader(List<Header> paramList, boolean paramBoolean) throws HttpException, IOException;
  
  void updateInputCapacity() throws IOException;
  
  void consumeData(ByteBuffer paramByteBuffer, boolean paramBoolean) throws HttpException, IOException;
  
  HandlerFactory<AsyncPushConsumer> getPushHandlerFactory();
  
  void failed(Exception paramException);
  
  void handle(HttpException paramHttpException, boolean paramBoolean) throws HttpException, IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/H2StreamHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */