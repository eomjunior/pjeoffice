package org.apache.hc.core5.http;

import java.util.Iterator;

public interface MessageHeaders {
  boolean containsHeader(String paramString);
  
  int countHeaders(String paramString);
  
  Header getFirstHeader(String paramString);
  
  Header getHeader(String paramString) throws ProtocolException;
  
  Header[] getHeaders();
  
  Header[] getHeaders(String paramString);
  
  Header getLastHeader(String paramString);
  
  Iterator<Header> headerIterator();
  
  Iterator<Header> headerIterator(String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/MessageHeaders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */