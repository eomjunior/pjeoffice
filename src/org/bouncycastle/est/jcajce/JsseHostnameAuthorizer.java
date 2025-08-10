package org.bouncycastle.est.jcajce;

import java.io.IOException;
import javax.net.ssl.SSLSession;

public interface JsseHostnameAuthorizer {
  boolean verified(String paramString, SSLSession paramSSLSession) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/est/jcajce/JsseHostnameAuthorizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */