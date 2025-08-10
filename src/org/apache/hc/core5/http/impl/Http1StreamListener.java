package org.apache.hc.core5.http.impl;

import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.Internal;
import org.apache.hc.core5.annotation.ThreadingBehavior;
import org.apache.hc.core5.http.HttpConnection;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;

@Contract(threading = ThreadingBehavior.STATELESS)
@Internal
public interface Http1StreamListener {
  void onRequestHead(HttpConnection paramHttpConnection, HttpRequest paramHttpRequest);
  
  void onResponseHead(HttpConnection paramHttpConnection, HttpResponse paramHttpResponse);
  
  void onExchangeComplete(HttpConnection paramHttpConnection, boolean paramBoolean);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/Http1StreamListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */