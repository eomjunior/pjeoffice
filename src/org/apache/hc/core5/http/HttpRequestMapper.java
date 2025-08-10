package org.apache.hc.core5.http;

import org.apache.hc.core5.http.protocol.HttpContext;

public interface HttpRequestMapper<T> {
  T resolve(HttpRequest paramHttpRequest, HttpContext paramHttpContext) throws HttpException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/HttpRequestMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */