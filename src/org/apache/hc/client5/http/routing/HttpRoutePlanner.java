package org.apache.hc.client5.http.routing;

import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.STATELESS)
public interface HttpRoutePlanner {
  HttpRoute determineRoute(HttpHost paramHttpHost, HttpContext paramHttpContext) throws HttpException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/routing/HttpRoutePlanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */