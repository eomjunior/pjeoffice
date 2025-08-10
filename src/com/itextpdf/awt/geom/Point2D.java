/*     */ package com.itextpdf.awt.geom;
/*     */ 
/*     */ import com.itextpdf.awt.geom.misc.HashCode;
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
/*     */ public abstract class Point2D
/*     */   implements Cloneable
/*     */ {
/*     */   public abstract double getX();
/*     */   
/*     */   public abstract double getY();
/*     */   
/*     */   public abstract void setLocation(double paramDouble1, double paramDouble2);
/*     */   
/*     */   public static class Float
/*     */     extends Point2D
/*     */   {
/*     */     public float x;
/*     */     public float y;
/*     */     
/*     */     public Float() {}
/*     */     
/*     */     public Float(float x, float y) {
/*  39 */       this.x = x;
/*  40 */       this.y = y;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX() {
/*  45 */       return this.x;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY() {
/*  50 */       return this.y;
/*     */     }
/*     */     
/*     */     public void setLocation(float x, float y) {
/*  54 */       this.x = x;
/*  55 */       this.y = y;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setLocation(double x, double y) {
/*  60 */       this.x = (float)x;
/*  61 */       this.y = (float)y;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  66 */       return getClass().getName() + "[x=" + this.x + ",y=" + this.y + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Double
/*     */     extends Point2D
/*     */   {
/*     */     public double x;
/*     */     public double y;
/*     */     
/*     */     public Double() {}
/*     */     
/*     */     public Double(double x, double y) {
/*  79 */       this.x = x;
/*  80 */       this.y = y;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX() {
/*  85 */       return this.x;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY() {
/*  90 */       return this.y;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setLocation(double x, double y) {
/*  95 */       this.x = x;
/*  96 */       this.y = y;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 101 */       return getClass().getName() + "[x=" + this.x + ",y=" + this.y + "]";
/*     */     }
/*     */   }
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
/*     */   public void setLocation(Point2D p) {
/* 115 */     setLocation(p.getX(), p.getY());
/*     */   }
/*     */   
/*     */   public static double distanceSq(double x1, double y1, double x2, double y2) {
/* 119 */     x2 -= x1;
/* 120 */     y2 -= y1;
/* 121 */     return x2 * x2 + y2 * y2;
/*     */   }
/*     */   
/*     */   public double distanceSq(double px, double py) {
/* 125 */     return distanceSq(getX(), getY(), px, py);
/*     */   }
/*     */   
/*     */   public double distanceSq(Point2D p) {
/* 129 */     return distanceSq(getX(), getY(), p.getX(), p.getY());
/*     */   }
/*     */   
/*     */   public static double distance(double x1, double y1, double x2, double y2) {
/* 133 */     return Math.sqrt(distanceSq(x1, y1, x2, y2));
/*     */   }
/*     */   
/*     */   public double distance(double px, double py) {
/* 137 */     return Math.sqrt(distanceSq(px, py));
/*     */   }
/*     */   
/*     */   public double distance(Point2D p) {
/* 141 */     return Math.sqrt(distanceSq(p));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 147 */       return super.clone();
/* 148 */     } catch (CloneNotSupportedException e) {
/* 149 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 155 */     HashCode hash = new HashCode();
/* 156 */     hash.append(getX());
/* 157 */     hash.append(getY());
/* 158 */     return hash.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 163 */     if (obj == this) {
/* 164 */       return true;
/*     */     }
/* 166 */     if (obj instanceof Point2D) {
/* 167 */       Point2D p = (Point2D)obj;
/* 168 */       return (getX() == p.getX() && getY() == p.getY());
/*     */     } 
/* 170 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/Point2D.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */