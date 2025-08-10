package org.apache.hc.core5.http;

import java.net.URI;

public interface HttpRequestFactory<T extends HttpRequest> {
  T newHttpRequest(String paramString1, String paramString2) throws MethodNotSupportedException;
  
  T newHttpRequest(String paramString, URI paramURI) throws MethodNotSupportedException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/HttpRequestFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */