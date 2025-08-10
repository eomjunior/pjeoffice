/*     */ package com.itextpdf.awt.geom;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Line2D;
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
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
/*     */ public class PolylineShape
/*     */   implements Shape
/*     */ {
/*     */   protected int[] x;
/*     */   protected int[] y;
/*     */   protected int np;
/*     */   
/*     */   public PolylineShape(int[] x, int[] y, int nPoints) {
/*  70 */     this.np = nPoints;
/*     */     
/*  72 */     this.x = new int[this.np];
/*  73 */     this.y = new int[this.np];
/*  74 */     System.arraycopy(x, 0, this.x, 0, this.np);
/*  75 */     System.arraycopy(y, 0, this.y, 0, this.np);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBounds2D() {
/*  86 */     int[] r = rect();
/*  87 */     return (r == null) ? null : new Rectangle2D.Double(r[0], r[1], r[2], r[3]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getBounds() {
/*  95 */     return getBounds2D().getBounds();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] rect() {
/* 104 */     if (this.np == 0) return null; 
/* 105 */     int xMin = this.x[0], yMin = this.y[0], xMax = this.x[0], yMax = this.y[0];
/*     */     
/* 107 */     for (int i = 1; i < this.np; i++) {
/* 108 */       if (this.x[i] < xMin) { xMin = this.x[i]; }
/* 109 */       else if (this.x[i] > xMax) { xMax = this.x[i]; }
/* 110 */        if (this.y[i] < yMin) { yMin = this.y[i]; }
/* 111 */       else if (this.y[i] > yMax) { yMax = this.y[i]; }
/*     */     
/*     */     } 
/* 114 */     return new int[] { xMin, yMin, xMax - xMin, yMax - yMin };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(double x, double y) {
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Point2D p) {
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(double x, double y, double w, double h) {
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Rectangle2D r) {
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(double x, double y, double w, double h) {
/* 147 */     return intersects(new Rectangle2D.Double(x, y, w, h));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(Rectangle2D r) {
/* 156 */     if (this.np == 0) return false; 
/* 157 */     Line2D line = new Line2D.Double(this.x[0], this.y[0], this.x[0], this.y[0]);
/* 158 */     for (int i = 1; i < this.np; i++) {
/* 159 */       line.setLine(this.x[i - 1], this.y[i - 1], this.x[i], this.y[i]);
/* 160 */       if (line.intersects(r)) return true; 
/*     */     } 
/* 162 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathIterator getPathIterator(AffineTransform at) {
/* 172 */     return new PolylineShapeIterator(this, at);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathIterator getPathIterator(AffineTransform at, double flatness) {
/* 180 */     return new PolylineShapeIterator(this, at);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/PolylineShape.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */