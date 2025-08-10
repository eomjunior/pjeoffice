package com.itextpdf.text;

public interface DocListener extends ElementListener {
  void open();
  
  void close();
  
  boolean newPage();
  
  boolean setPageSize(Rectangle paramRectangle);
  
  boolean setMargins(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  boolean setMarginMirroring(boolean paramBoolean);
  
  boolean setMarginMirroringTopBottom(boolean paramBoolean);
  
  void setPageCount(int paramInt);
  
  void resetPageCount();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/DocListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */