package org.apache.hc.core5.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface TrustStrategy {
  boolean isTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/ssl/TrustStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */