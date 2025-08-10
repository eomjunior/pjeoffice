/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.awt.geom.AffineTransform;
/*     */ import com.itextpdf.text.Rectangle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfRectangle
/*     */   extends NumberArray
/*     */ {
/*  68 */   private float llx = 0.0F;
/*     */ 
/*     */   
/*  71 */   private float lly = 0.0F;
/*     */ 
/*     */   
/*  74 */   private float urx = 0.0F;
/*     */ 
/*     */   
/*  77 */   private float ury = 0.0F;
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
/*     */   public PdfRectangle(float llx, float lly, float urx, float ury, int rotation) {
/*  93 */     super(new float[0]);
/*  94 */     if (rotation == 90 || rotation == 270) {
/*  95 */       this.llx = lly;
/*  96 */       this.lly = llx;
/*  97 */       this.urx = ury;
/*  98 */       this.ury = urx;
/*     */     } else {
/*     */       
/* 101 */       this.llx = llx;
/* 102 */       this.lly = lly;
/* 103 */       this.urx = urx;
/* 104 */       this.ury = ury;
/*     */     } 
/* 106 */     super.add(new PdfNumber(this.llx));
/* 107 */     super.add(new PdfNumber(this.lly));
/* 108 */     super.add(new PdfNumber(this.urx));
/* 109 */     super.add(new PdfNumber(this.ury));
/*     */   }
/*     */   
/*     */   public PdfRectangle(float llx, float lly, float urx, float ury) {
/* 113 */     this(llx, lly, urx, ury, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfRectangle(float urx, float ury, int rotation) {
/* 124 */     this(0.0F, 0.0F, urx, ury, rotation);
/*     */   }
/*     */   
/*     */   public PdfRectangle(float urx, float ury) {
/* 128 */     this(0.0F, 0.0F, urx, ury, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfRectangle(Rectangle rectangle, int rotation) {
/* 138 */     this(rectangle.getLeft(), rectangle.getBottom(), rectangle.getRight(), rectangle.getTop(), rotation);
/*     */   }
/*     */   
/*     */   public PdfRectangle(Rectangle rectangle) {
/* 142 */     this(rectangle.getLeft(), rectangle.getBottom(), rectangle.getRight(), rectangle.getTop(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getRectangle() {
/* 151 */     return new Rectangle(left(), bottom(), right(), top());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(PdfObject object) {
/* 162 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(float[] values) {
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(int[] values) {
/* 184 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFirst(PdfObject object) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float left() {
/* 202 */     return this.llx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float right() {
/* 212 */     return this.urx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float top() {
/* 222 */     return this.ury;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float bottom() {
/* 232 */     return this.lly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float left(int margin) {
/* 243 */     return this.llx + margin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float right(int margin) {
/* 254 */     return this.urx - margin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float top(int margin) {
/* 265 */     return this.ury - margin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float bottom(int margin) {
/* 276 */     return this.lly + margin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float width() {
/* 286 */     return this.urx - this.llx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float height() {
/* 296 */     return this.ury - this.lly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfRectangle rotate() {
/* 306 */     return new PdfRectangle(this.lly, this.llx, this.ury, this.urx, 0);
/*     */   }
/*     */   
/*     */   public PdfRectangle transform(AffineTransform transform) {
/* 310 */     float[] pts = { this.llx, this.lly, this.urx, this.ury };
/* 311 */     transform.transform(pts, 0, pts, 0, 2);
/* 312 */     float[] dstPts = { pts[0], pts[1], pts[2], pts[3] };
/* 313 */     if (pts[0] > pts[2]) {
/* 314 */       dstPts[0] = pts[2];
/* 315 */       dstPts[2] = pts[0];
/*     */     } 
/* 317 */     if (pts[1] > pts[3]) {
/* 318 */       dstPts[1] = pts[3];
/* 319 */       dstPts[3] = pts[1];
/*     */     } 
/* 321 */     return new PdfRectangle(dstPts[0], dstPts[1], dstPts[2], dstPts[3]);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfRectangle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */