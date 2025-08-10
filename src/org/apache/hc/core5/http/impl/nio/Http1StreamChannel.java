package org.apache.hc.core5.http.impl.nio;

import java.io.IOException;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.nio.ContentEncoder;
import org.apache.hc.core5.util.Timeout;

interface Http1StreamChannel<OutgoingMessage extends org.apache.hc.core5.http.HttpMessage> extends ContentEncoder {
  void close();
  
  void activate() throws HttpException, IOException;
  
  void submit(OutgoingMessage paramOutgoingMessage, boolean paramBoolean, FlushMode paramFlushMode) throws HttpException, IOException;
  
  void requestOutput();
  
  void suspendOutput() throws IOException;
  
  boolean abortGracefully() throws IOException;
  
  Timeout getSocketTimeout();
  
  void setSocketTimeout(Timeout paramTimeout);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/Http1StreamChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */