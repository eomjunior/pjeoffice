package org.apache.hc.core5.http;

import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;
import org.apache.hc.core5.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.STATELESS)
public interface ConnectionReuseStrategy {
  boolean keepAlive(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, HttpContext paramHttpContext);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/ConnectionReuseStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */