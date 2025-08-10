package org.apache.hc.client5.http.cookie;

import java.util.List;
import org.apache.hc.core5.http.Header;

public interface CookieSpec {
  List<Cookie> parse(Header paramHeader, CookieOrigin paramCookieOrigin) throws MalformedCookieException;
  
  void validate(Cookie paramCookie, CookieOrigin paramCookieOrigin) throws MalformedCookieException;
  
  boolean match(Cookie paramCookie, CookieOrigin paramCookieOrigin);
  
  List<Header> formatCookies(List<Cookie> paramList);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/cookie/CookieSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */