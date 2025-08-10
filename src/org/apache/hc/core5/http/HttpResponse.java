package org.apache.hc.core5.http;

import java.util.Locale;

public interface HttpResponse extends HttpMessage {
  int getCode();
  
  void setCode(int paramInt);
  
  String getReasonPhrase();
  
  void setReasonPhrase(String paramString);
  
  Locale getLocale();
  
  void setLocale(Locale paramLocale);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/HttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */