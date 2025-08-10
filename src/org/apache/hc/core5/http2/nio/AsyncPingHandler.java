package org.apache.hc.core5.http2.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.hc.core5.http.HttpException;

public interface AsyncPingHandler {
  ByteBuffer getData();
  
  void consumeResponse(ByteBuffer paramByteBuffer) throws HttpException, IOException;
  
  void failed(Exception paramException);
  
  void cancel();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/nio/AsyncPingHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */