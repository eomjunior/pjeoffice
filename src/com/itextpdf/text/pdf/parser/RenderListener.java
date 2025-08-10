package com.itextpdf.text.pdf.parser;

public interface RenderListener {
  void beginTextBlock();
  
  void renderText(TextRenderInfo paramTextRenderInfo);
  
  void endTextBlock();
  
  void renderImage(ImageRenderInfo paramImageRenderInfo);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/RenderListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */