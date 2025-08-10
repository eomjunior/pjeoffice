/*     */ package com.itextpdf.awt.geom;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class Point
/*     */   extends Point2D
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5276940640259749850L;
/*     */   public double x;
/*     */   public double y;
/*     */   
/*     */   public Point() {
/*  36 */     setLocation(0, 0);
/*     */   }
/*     */   
/*     */   public Point(int x, int y) {
/*  40 */     setLocation(x, y);
/*     */   }
/*     */   
/*     */   public Point(double x, double y) {
/*  44 */     setLocation(x, y);
/*     */   }
/*     */   
/*     */   public Point(Point p) {
/*  48 */     setLocation(p.x, p.y);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  53 */     if (obj == this) {
/*  54 */       return true;
/*     */     }
/*  56 */     if (obj instanceof Point) {
/*  57 */       Point p = (Point)obj;
/*  58 */       return (this.x == p.x && this.y == p.y);
/*     */     } 
/*  60 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  65 */     return getClass().getName() + "[x=" + this.x + ",y=" + this.y + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public double getX() {
/*  70 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getY() {
/*  75 */     return this.y;
/*     */   }
/*     */   
/*     */   public Point getLocation() {
/*  79 */     return new Point(this.x, this.y);
/*     */   }
/*     */   
/*     */   public void setLocation(Point p) {
/*  83 */     setLocation(p.x, p.y);
/*     */   }
/*     */   
/*     */   public void setLocation(int x, int y) {
/*  87 */     setLocation(x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLocation(double x, double y) {
/*  92 */     this.x = x;
/*  93 */     this.y = y;
/*     */   }
/*     */   
/*     */   public void move(int x, int y) {
/*  97 */     move(x, y);
/*     */   }
/*     */   public void move(double x, double y) {
/* 100 */     setLocation(x, y);
/*     */   }
/*     */   
/*     */   public void translate(int dx, int dy) {
/* 104 */     translate(dx, dy);
/*     */   }
/*     */   public void translate(double dx, double dy) {
/* 107 */     this.x += dx;
/* 108 */     this.y += dy;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/Point.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */