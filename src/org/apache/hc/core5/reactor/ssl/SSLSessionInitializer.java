package org.apache.hc.core5.reactor.ssl;

import javax.net.ssl.SSLEngine;
import org.apache.hc.core5.net.NamedEndpoint;

public interface SSLSessionInitializer {
  void initialize(NamedEndpoint paramNamedEndpoint, SSLEngine paramSSLEngine);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/ssl/SSLSessionInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */