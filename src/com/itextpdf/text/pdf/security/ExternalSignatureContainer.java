package com.itextpdf.text.pdf.security;

import com.itextpdf.text.pdf.PdfDictionary;
import java.io.InputStream;
import java.security.GeneralSecurityException;

public interface ExternalSignatureContainer {
  byte[] sign(InputStream paramInputStream) throws GeneralSecurityException;
  
  void modifySigningDictionary(PdfDictionary paramPdfDictionary);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/ExternalSignatureContainer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */