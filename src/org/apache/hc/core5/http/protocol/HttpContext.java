package org.apache.hc.core5.http.protocol;

import org.apache.hc.core5.http.ProtocolVersion;

public interface HttpContext {
  public static final String RESERVED_PREFIX = "http.";
  
  ProtocolVersion getProtocolVersion();
  
  void setProtocolVersion(ProtocolVersion paramProtocolVersion);
  
  Object getAttribute(String paramString);
  
  Object setAttribute(String paramString, Object paramObject);
  
  Object removeAttribute(String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/HttpContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */