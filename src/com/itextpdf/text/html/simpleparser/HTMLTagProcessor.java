package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.util.Map;

@Deprecated
public interface HTMLTagProcessor {
  void startElement(HTMLWorker paramHTMLWorker, String paramString, Map<String, String> paramMap) throws DocumentException, IOException;
  
  void endElement(HTMLWorker paramHTMLWorker, String paramString) throws DocumentException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/html/simpleparser/HTMLTagProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */