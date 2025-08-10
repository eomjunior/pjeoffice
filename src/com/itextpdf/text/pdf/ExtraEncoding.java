package com.itextpdf.text.pdf;

public interface ExtraEncoding {
  byte[] charToByte(String paramString1, String paramString2);
  
  byte[] charToByte(char paramChar, String paramString);
  
  String byteToChar(byte[] paramArrayOfbyte, String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/ExtraEncoding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */