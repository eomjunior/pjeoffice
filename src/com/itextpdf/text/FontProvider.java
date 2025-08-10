package com.itextpdf.text;

public interface FontProvider {
  boolean isRegistered(String paramString);
  
  Font getFont(String paramString1, String paramString2, boolean paramBoolean, float paramFloat, int paramInt, BaseColor paramBaseColor);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/FontProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */