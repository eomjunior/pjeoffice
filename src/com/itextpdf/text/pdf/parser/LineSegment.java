/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import com.itextpdf.awt.geom.Rectangle2D;
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
/*     */ public class LineSegment
/*     */ {
/*     */   private final Vector startPoint;
/*     */   private final Vector endPoint;
/*     */   
/*     */   public LineSegment(Vector startPoint, Vector endPoint) {
/*  65 */     this.startPoint = startPoint;
/*  66 */     this.endPoint = endPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getStartPoint() {
/*  73 */     return this.startPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getEndPoint() {
/*  80 */     return this.endPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getLength() {
/*  88 */     return this.endPoint.subtract(this.startPoint).length();
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
/*     */   public Rectangle2D.Float getBoundingRectange() {
/* 100 */     float x1 = getStartPoint().get(0);
/* 101 */     float y1 = getStartPoint().get(1);
/* 102 */     float x2 = getEndPoint().get(0);
/* 103 */     float y2 = getEndPoint().get(1);
/* 104 */     return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineSegment transformBy(Matrix m) {
/* 114 */     Vector newStart = this.startPoint.cross(m);
/* 115 */     Vector newEnd = this.endPoint.cross(m);
/* 116 */     return new LineSegment(newStart, newEnd);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/LineSegment.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */