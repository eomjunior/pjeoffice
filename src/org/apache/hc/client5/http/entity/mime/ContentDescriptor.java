package org.apache.hc.client5.http.entity.mime;

public interface ContentDescriptor {
  String getMimeType();
  
  String getMediaType();
  
  String getSubType();
  
  String getCharset();
  
  long getContentLength();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/ContentDescriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */