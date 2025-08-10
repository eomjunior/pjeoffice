package org.apache.hc.core5.http;

public interface HttpResponseFactory<T extends HttpResponse> {
  T newHttpResponse(int paramInt, String paramString);
  
  T newHttpResponse(int paramInt);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/HttpResponseFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */