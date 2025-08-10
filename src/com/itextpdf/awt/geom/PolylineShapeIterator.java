/*     */ package com.itextpdf.awt.geom;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class PolylineShapeIterator
/*     */   implements PathIterator
/*     */ {
/*     */   protected PolylineShape poly;
/*     */   protected AffineTransform affine;
/*     */   protected int index;
/*     */   
/*     */   PolylineShapeIterator(PolylineShape l, AffineTransform at) {
/*  65 */     this.poly = l;
/*  66 */     this.affine = at;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int currentSegment(double[] coords) {
/*  88 */     if (isDone()) {
/*  89 */       throw new NoSuchElementException(MessageLocalization.getComposedMessage("line.iterator.out.of.bounds", new Object[0]));
/*     */     }
/*  91 */     int type = (this.index == 0) ? 0 : 1;
/*  92 */     coords[0] = this.poly.x[this.index];
/*  93 */     coords[1] = this.poly.y[this.index];
/*  94 */     if (this.affine != null) {
/*  95 */       this.affine.transform(coords, 0, coords, 0, 1);
/*     */     }
/*  97 */     return type;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int currentSegment(float[] coords) {
/* 119 */     if (isDone()) {
/* 120 */       throw new NoSuchElementException(MessageLocalization.getComposedMessage("line.iterator.out.of.bounds", new Object[0]));
/*     */     }
/* 122 */     int type = (this.index == 0) ? 0 : 1;
/* 123 */     coords[0] = this.poly.x[this.index];
/* 124 */     coords[1] = this.poly.y[this.index];
/* 125 */     if (this.affine != null) {
/* 126 */       this.affine.transform(coords, 0, coords, 0, 1);
/*     */     }
/* 128 */     return type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWindingRule() {
/* 139 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/* 148 */     return (this.index >= this.poly.np);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void next() {
/* 158 */     this.index++;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/PolylineShapeIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */