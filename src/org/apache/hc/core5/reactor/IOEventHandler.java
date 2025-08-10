package org.apache.hc.core5.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.hc.core5.annotation.Internal;
import org.apache.hc.core5.util.Timeout;

@Internal
public interface IOEventHandler {
  void connected(IOSession paramIOSession) throws IOException;
  
  void inputReady(IOSession paramIOSession, ByteBuffer paramByteBuffer) throws IOException;
  
  void outputReady(IOSession paramIOSession) throws IOException;
  
  void timeout(IOSession paramIOSession, Timeout paramTimeout) throws IOException;
  
  void exception(IOSession paramIOSession, Exception paramException);
  
  void disconnected(IOSession paramIOSession);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/IOEventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */