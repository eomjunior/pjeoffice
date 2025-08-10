package org.apache.hc.core5.reactor.ssl;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import org.apache.hc.core5.net.NamedEndpoint;

public interface SSLSessionVerifier {
  TlsDetails verify(NamedEndpoint paramNamedEndpoint, SSLEngine paramSSLEngine) throws SSLException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/ssl/SSLSessionVerifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */