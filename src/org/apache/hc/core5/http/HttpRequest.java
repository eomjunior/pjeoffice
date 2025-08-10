package org.apache.hc.core5.http;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.hc.core5.net.URIAuthority;

public interface HttpRequest extends HttpMessage {
  String getMethod();
  
  String getPath();
  
  void setPath(String paramString);
  
  String getScheme();
  
  void setScheme(String paramString);
  
  URIAuthority getAuthority();
  
  void setAuthority(URIAuthority paramURIAuthority);
  
  String getRequestUri();
  
  URI getUri() throws URISyntaxException;
  
  void setUri(URI paramURI);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/HttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */