package org.apache.hc.client5.http.auth;

import org.apache.hc.core5.http.protocol.HttpContext;

public interface AuthSchemeFactory {
  AuthScheme create(HttpContext paramHttpContext);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/AuthSchemeFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */