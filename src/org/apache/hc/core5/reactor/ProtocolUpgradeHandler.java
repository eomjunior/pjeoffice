package org.apache.hc.core5.reactor;

import org.apache.hc.core5.annotation.Internal;
import org.apache.hc.core5.concurrent.FutureCallback;

@Internal
public interface ProtocolUpgradeHandler {
  void upgrade(ProtocolIOSession paramProtocolIOSession, FutureCallback<ProtocolIOSession> paramFutureCallback);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/ProtocolUpgradeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */