package com.itextpdf.awt.geom;

public interface PathIterator {
  public static final int WIND_EVEN_ODD = 0;
  
  public static final int WIND_NON_ZERO = 1;
  
  public static final int SEG_MOVETO = 0;
  
  public static final int SEG_LINETO = 1;
  
  public static final int SEG_QUADTO = 2;
  
  public static final int SEG_CUBICTO = 3;
  
  public static final int SEG_CLOSE = 4;
  
  int getWindingRule();
  
  boolean isDone();
  
  void next();
  
  int currentSegment(float[] paramArrayOffloat);
  
  int currentSegment(double[] paramArrayOfdouble);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/PathIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */