package org.apache.hc.core5.pool;

import java.util.concurrent.Future;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.util.Timeout;

public interface ConnPool<T, C extends org.apache.hc.core5.io.ModalCloseable> {
  Future<PoolEntry<T, C>> lease(T paramT, Object paramObject, Timeout paramTimeout, FutureCallback<PoolEntry<T, C>> paramFutureCallback);
  
  void release(PoolEntry<T, C> paramPoolEntry, boolean paramBoolean);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/pool/ConnPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */