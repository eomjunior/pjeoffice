package org.apache.hc.core5.reactor;

import java.net.SocketAddress;
import java.util.concurrent.Future;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.net.NamedEndpoint;
import org.apache.hc.core5.util.Timeout;

public interface ConnectionInitiator {
  Future<IOSession> connect(NamedEndpoint paramNamedEndpoint, SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, Timeout paramTimeout, Object paramObject, FutureCallback<IOSession> paramFutureCallback);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/ConnectionInitiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */