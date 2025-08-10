package org.apache.hc.core5.pool;

import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;

@Contract(threading = ThreadingBehavior.STATELESS)
public interface ConnPoolListener<T> {
  void onLease(T paramT, ConnPoolStats<T> paramConnPoolStats);
  
  void onRelease(T paramT, ConnPoolStats<T> paramConnPoolStats);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/pool/ConnPoolListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */