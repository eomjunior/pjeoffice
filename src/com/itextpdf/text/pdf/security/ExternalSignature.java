package com.itextpdf.text.pdf.security;

import java.security.GeneralSecurityException;

public interface ExternalSignature {
  String getHashAlgorithm();
  
  String getEncryptionAlgorithm();
  
  byte[] sign(byte[] paramArrayOfbyte) throws GeneralSecurityException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/ExternalSignature.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */