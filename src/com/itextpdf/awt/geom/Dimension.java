/*     */ package com.itextpdf.awt.geom;
/*     */ 
/*     */ import com.itextpdf.awt.geom.misc.HashCode;
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
/*     */ 
/*     */ 
/*     */ public class Dimension
/*     */   extends Dimension2D
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4723952579491349524L;
/*     */   public double width;
/*     */   public double height;
/*     */   
/*     */   public Dimension(Dimension d) {
/*  39 */     this(d.width, d.height);
/*     */   }
/*     */   
/*     */   public Dimension() {
/*  43 */     this(0, 0);
/*     */   }
/*     */   
/*     */   public Dimension(double width, double height) {
/*  47 */     setSize(width, height);
/*     */   }
/*     */   
/*     */   public Dimension(int width, int height) {
/*  51 */     setSize(width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  56 */     HashCode hash = new HashCode();
/*  57 */     hash.append(this.width);
/*  58 */     hash.append(this.height);
/*  59 */     return hash.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  64 */     if (obj == this) {
/*  65 */       return true;
/*     */     }
/*  67 */     if (obj instanceof Dimension) {
/*  68 */       Dimension d = (Dimension)obj;
/*  69 */       return (d.width == this.width && d.height == this.height);
/*     */     } 
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  78 */     return getClass().getName() + "[width=" + this.width + ",height=" + this.height + "]";
/*     */   }
/*     */   
/*     */   public void setSize(int width, int height) {
/*  82 */     this.width = width;
/*  83 */     this.height = height;
/*     */   }
/*     */   
/*     */   public void setSize(Dimension d) {
/*  87 */     setSize(d.width, d.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSize(double width, double height) {
/*  92 */     setSize((int)Math.ceil(width), (int)Math.ceil(height));
/*     */   }
/*     */   
/*     */   public Dimension getSize() {
/*  96 */     return new Dimension(this.width, this.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getHeight() {
/* 101 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getWidth() {
/* 106 */     return this.width;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/Dimension.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */