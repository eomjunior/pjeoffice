package org.apache.hc.core5.http.nio.ssl;

import org.apache.hc.core5.annotation.Internal;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.net.NamedEndpoint;
import org.apache.hc.core5.reactor.ProtocolIOSession;

@Internal
public interface TlsUpgradeCapable {
  void tlsUpgrade(NamedEndpoint paramNamedEndpoint, FutureCallback<ProtocolIOSession> paramFutureCallback);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/ssl/TlsUpgradeCapable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */