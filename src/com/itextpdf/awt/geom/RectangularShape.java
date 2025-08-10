/*     */ package com.itextpdf.awt.geom;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RectangularShape
/*     */   implements Shape, Cloneable
/*     */ {
/*     */   public abstract double getX();
/*     */   
/*     */   public abstract double getY();
/*     */   
/*     */   public abstract double getWidth();
/*     */   
/*     */   public abstract double getHeight();
/*     */   
/*     */   public abstract boolean isEmpty();
/*     */   
/*     */   public abstract void setFrame(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*     */   
/*     */   public double getMinX() {
/*  44 */     return getX();
/*     */   }
/*     */   
/*     */   public double getMinY() {
/*  48 */     return getY();
/*     */   }
/*     */   
/*     */   public double getMaxX() {
/*  52 */     return getX() + getWidth();
/*     */   }
/*     */   
/*     */   public double getMaxY() {
/*  56 */     return getY() + getHeight();
/*     */   }
/*     */   
/*     */   public double getCenterX() {
/*  60 */     return getX() + getWidth() / 2.0D;
/*     */   }
/*     */   
/*     */   public double getCenterY() {
/*  64 */     return getY() + getHeight() / 2.0D;
/*     */   }
/*     */   
/*     */   public Rectangle2D getFrame() {
/*  68 */     return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
/*     */   }
/*     */   
/*     */   public void setFrame(Point2D loc, Dimension2D size) {
/*  72 */     setFrame(loc.getX(), loc.getY(), size.getWidth(), size.getHeight());
/*     */   }
/*     */   
/*     */   public void setFrame(Rectangle2D r) {
/*  76 */     setFrame(r.getX(), r.getY(), r.getWidth(), r.getHeight());
/*     */   }
/*     */   
/*     */   public void setFrameFromDiagonal(double x1, double y1, double x2, double y2) {
/*     */     double rx, ry, rw, rh;
/*  81 */     if (x1 < x2) {
/*  82 */       rx = x1;
/*  83 */       rw = x2 - x1;
/*     */     } else {
/*  85 */       rx = x2;
/*  86 */       rw = x1 - x2;
/*     */     } 
/*  88 */     if (y1 < y2) {
/*  89 */       ry = y1;
/*  90 */       rh = y2 - y1;
/*     */     } else {
/*  92 */       ry = y2;
/*  93 */       rh = y1 - y2;
/*     */     } 
/*  95 */     setFrame(rx, ry, rw, rh);
/*     */   }
/*     */   
/*     */   public void setFrameFromDiagonal(Point2D p1, Point2D p2) {
/*  99 */     setFrameFromDiagonal(p1.getX(), p1.getY(), p2.getX(), p2.getY());
/*     */   }
/*     */   
/*     */   public void setFrameFromCenter(double centerX, double centerY, double cornerX, double cornerY) {
/* 103 */     double width = Math.abs(cornerX - centerX);
/* 104 */     double height = Math.abs(cornerY - centerY);
/* 105 */     setFrame(centerX - width, centerY - height, width * 2.0D, height * 2.0D);
/*     */   }
/*     */   
/*     */   public void setFrameFromCenter(Point2D center, Point2D corner) {
/* 109 */     setFrameFromCenter(center.getX(), center.getY(), corner.getX(), corner.getY());
/*     */   }
/*     */   
/*     */   public boolean contains(Point2D point) {
/* 113 */     return contains(point.getX(), point.getY());
/*     */   }
/*     */   
/*     */   public boolean intersects(Rectangle2D rect) {
/* 117 */     return intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
/*     */   }
/*     */   
/*     */   public boolean contains(Rectangle2D rect) {
/* 121 */     return contains(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
/*     */   }
/*     */   
/*     */   public Rectangle getBounds() {
/* 125 */     int x1 = (int)Math.floor(getMinX());
/* 126 */     int y1 = (int)Math.floor(getMinY());
/* 127 */     int x2 = (int)Math.ceil(getMaxX());
/* 128 */     int y2 = (int)Math.ceil(getMaxY());
/* 129 */     return new Rectangle(x1, y1, (x2 - x1), (y2 - y1));
/*     */   }
/*     */   
/*     */   public PathIterator getPathIterator(AffineTransform t, double flatness) {
/* 133 */     return new FlatteningPathIterator(getPathIterator(t), flatness);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 139 */       return super.clone();
/* 140 */     } catch (CloneNotSupportedException e) {
/* 141 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/RectangularShape.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */