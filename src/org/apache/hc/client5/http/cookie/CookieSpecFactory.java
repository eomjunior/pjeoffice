package org.apache.hc.client5.http.cookie;

import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;
import org.apache.hc.core5.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.STATELESS)
public interface CookieSpecFactory {
  CookieSpec create(HttpContext paramHttpContext);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/cookie/CookieSpecFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */