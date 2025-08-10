package com.itextpdf.awt;

import com.itextpdf.text.pdf.BaseFont;
import java.awt.Font;

public interface FontMapper {
  BaseFont awtToPdf(Font paramFont);
  
  Font pdfToAwt(BaseFont paramBaseFont, int paramInt);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/FontMapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */