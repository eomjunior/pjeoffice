package com.itextpdf.text.pdf.parser;

public interface ExtRenderListener extends RenderListener {
  void modifyPath(PathConstructionRenderInfo paramPathConstructionRenderInfo);
  
  Path renderPath(PathPaintingRenderInfo paramPathPaintingRenderInfo);
  
  void clipPath(int paramInt);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/ExtRenderListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */