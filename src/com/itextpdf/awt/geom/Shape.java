package com.itextpdf.awt.geom;

public interface Shape {
  boolean contains(double paramDouble1, double paramDouble2);
  
  boolean contains(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
  
  boolean contains(Point2D paramPoint2D);
  
  boolean contains(Rectangle2D paramRectangle2D);
  
  Rectangle getBounds();
  
  Rectangle2D getBounds2D();
  
  PathIterator getPathIterator(AffineTransform paramAffineTransform);
  
  PathIterator getPathIterator(AffineTransform paramAffineTransform, double paramDouble);
  
  boolean intersects(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
  
  boolean intersects(Rectangle2D paramRectangle2D);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/Shape.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */