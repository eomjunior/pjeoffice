package com.itextpdf.text.xml.simpleparser;

import java.util.Map;

public interface SimpleXMLDocHandler {
  void startElement(String paramString, Map<String, String> paramMap);
  
  void endElement(String paramString);
  
  void startDocument();
  
  void endDocument();
  
  void text(String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/simpleparser/SimpleXMLDocHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */