package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.DocumentException;
import java.security.cert.Certificate;

public interface PdfEncryptionSettings {
  void setEncryption(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt1, int paramInt2) throws DocumentException;
  
  void setEncryption(Certificate[] paramArrayOfCertificate, int[] paramArrayOfint, int paramInt) throws DocumentException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/interfaces/PdfEncryptionSettings.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */