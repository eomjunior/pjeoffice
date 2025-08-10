package org.apache.hc.core5.http.nio;

import java.io.IOException;
import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;

@Contract(threading = ThreadingBehavior.SAFE)
public interface CapacityChannel {
  void update(int paramInt) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/CapacityChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */