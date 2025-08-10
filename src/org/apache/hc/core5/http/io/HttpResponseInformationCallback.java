package org.apache.hc.core5.http.io;

import org.apache.hc.core5.http.HttpConnection;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.protocol.HttpContext;

public interface HttpResponseInformationCallback {
  void execute(HttpResponse paramHttpResponse, HttpConnection paramHttpConnection, HttpContext paramHttpContext) throws HttpException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/HttpResponseInformationCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */