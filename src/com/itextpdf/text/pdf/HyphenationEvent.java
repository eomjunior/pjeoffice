package com.itextpdf.text.pdf;

public interface HyphenationEvent {
  String getHyphenSymbol();
  
  String getHyphenatedWordPre(String paramString, BaseFont paramBaseFont, float paramFloat1, float paramFloat2);
  
  String getHyphenatedWordPost();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/HyphenationEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */