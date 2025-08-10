package org.apache.hc.core5.http;

public interface HttpMessage extends MessageHeaders {
  void setVersion(ProtocolVersion paramProtocolVersion);
  
  ProtocolVersion getVersion();
  
  void addHeader(Header paramHeader);
  
  void addHeader(String paramString, Object paramObject);
  
  void setHeader(Header paramHeader);
  
  void setHeader(String paramString, Object paramObject);
  
  void setHeaders(Header... paramVarArgs);
  
  boolean removeHeader(Header paramHeader);
  
  boolean removeHeaders(String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/HttpMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */