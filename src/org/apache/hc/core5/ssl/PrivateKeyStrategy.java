package org.apache.hc.core5.ssl;

import java.util.Map;
import javax.net.ssl.SSLParameters;

public interface PrivateKeyStrategy {
  String chooseAlias(Map<String, PrivateKeyDetails> paramMap, SSLParameters paramSSLParameters);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/ssl/PrivateKeyStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */