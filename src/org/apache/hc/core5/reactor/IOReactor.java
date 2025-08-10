package org.apache.hc.core5.reactor;

import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.io.ModalCloseable;
import org.apache.hc.core5.util.TimeValue;

public interface IOReactor extends ModalCloseable {
  void close(CloseMode paramCloseMode);
  
  IOReactorStatus getStatus();
  
  void initiateShutdown();
  
  void awaitShutdown(TimeValue paramTimeValue) throws InterruptedException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/IOReactor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */