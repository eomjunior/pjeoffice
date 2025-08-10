package com.itextpdf.text.pdf.security;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public interface TSAClient {
  int getTokenSizeEstimate();
  
  MessageDigest getMessageDigest() throws GeneralSecurityException;
  
  byte[] getTimeStampToken(byte[] paramArrayOfbyte) throws Exception;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/TSAClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */