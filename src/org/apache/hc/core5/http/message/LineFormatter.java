package org.apache.hc.core5.http.message;

import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.util.CharArrayBuffer;

public interface LineFormatter {
  void formatRequestLine(CharArrayBuffer paramCharArrayBuffer, RequestLine paramRequestLine);
  
  void formatStatusLine(CharArrayBuffer paramCharArrayBuffer, StatusLine paramStatusLine);
  
  void formatHeader(CharArrayBuffer paramCharArrayBuffer, Header paramHeader);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/LineFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */