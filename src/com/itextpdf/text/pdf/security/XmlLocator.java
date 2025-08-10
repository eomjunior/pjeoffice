package com.itextpdf.text.pdf.security;

import com.itextpdf.text.DocumentException;
import java.io.IOException;
import org.w3c.dom.Document;

public interface XmlLocator {
  Document getDocument();
  
  void setDocument(Document paramDocument) throws IOException, DocumentException;
  
  String getEncoding();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/XmlLocator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */