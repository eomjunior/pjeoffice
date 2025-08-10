package org.apache.hc.core5.reactor;

import org.apache.hc.core5.annotation.Internal;

@Internal
public interface IOSessionListener {
  void connected(IOSession paramIOSession);
  
  void startTls(IOSession paramIOSession);
  
  void inputReady(IOSession paramIOSession);
  
  void outputReady(IOSession paramIOSession);
  
  void timeout(IOSession paramIOSession);
  
  void exception(IOSession paramIOSession, Exception paramException);
  
  void disconnected(IOSession paramIOSession);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/IOSessionListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */