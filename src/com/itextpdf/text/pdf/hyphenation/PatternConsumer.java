package com.itextpdf.text.pdf.hyphenation;

import java.util.ArrayList;

public interface PatternConsumer {
  void addClass(String paramString);
  
  void addException(String paramString, ArrayList<Object> paramArrayList);
  
  void addPattern(String paramString1, String paramString2);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/hyphenation/PatternConsumer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */