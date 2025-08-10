package org.apache.hc.core5.http;

import java.util.Set;

public interface EntityDetails {
  long getContentLength();
  
  String getContentType();
  
  String getContentEncoding();
  
  boolean isChunked();
  
  Set<String> getTrailerNames();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/EntityDetails.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */