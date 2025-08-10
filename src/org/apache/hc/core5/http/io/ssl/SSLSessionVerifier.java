package org.apache.hc.core5.http.io.ssl;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import org.apache.hc.core5.http.HttpHost;

public interface SSLSessionVerifier {
  void verify(HttpHost paramHttpHost, SSLSession paramSSLSession) throws SSLException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/ssl/SSLSessionVerifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */