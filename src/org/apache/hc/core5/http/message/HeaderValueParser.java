package org.apache.hc.core5.http.message;

import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.NameValuePair;

public interface HeaderValueParser {
  HeaderElement[] parseElements(CharSequence paramCharSequence, ParserCursor paramParserCursor);
  
  HeaderElement parseHeaderElement(CharSequence paramCharSequence, ParserCursor paramParserCursor);
  
  NameValuePair[] parseParameters(CharSequence paramCharSequence, ParserCursor paramParserCursor);
  
  NameValuePair parseNameValuePair(CharSequence paramCharSequence, ParserCursor paramParserCursor);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/HeaderValueParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */