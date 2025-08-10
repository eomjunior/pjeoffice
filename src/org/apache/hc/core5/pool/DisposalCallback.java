package org.apache.hc.core5.pool;

import org.apache.hc.core5.annotation.Internal;
import org.apache.hc.core5.io.CloseMode;

@Internal
public interface DisposalCallback<T extends org.apache.hc.core5.io.ModalCloseable> {
  void execute(T paramT, CloseMode paramCloseMode);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/pool/DisposalCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */