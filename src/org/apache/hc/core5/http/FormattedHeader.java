package org.apache.hc.core5.http;

import org.apache.hc.core5.util.CharArrayBuffer;

public interface FormattedHeader extends Header {
  CharArrayBuffer getBuffer();
  
  int getValuePos();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/FormattedHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */