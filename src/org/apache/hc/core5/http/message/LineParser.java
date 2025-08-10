package org.apache.hc.core5.http.message;

import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.util.CharArrayBuffer;

public interface LineParser {
  RequestLine parseRequestLine(CharArrayBuffer paramCharArrayBuffer) throws ParseException;
  
  StatusLine parseStatusLine(CharArrayBuffer paramCharArrayBuffer) throws ParseException;
  
  Header parseHeader(CharArrayBuffer paramCharArrayBuffer) throws ParseException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/LineParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */