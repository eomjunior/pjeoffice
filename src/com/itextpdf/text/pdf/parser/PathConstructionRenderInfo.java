/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathConstructionRenderInfo
/*     */ {
/*     */   public static final int MOVETO = 1;
/*     */   public static final int LINETO = 2;
/*     */   public static final int CURVE_123 = 3;
/*     */   public static final int CURVE_23 = 4;
/*     */   public static final int CURVE_13 = 5;
/*     */   public static final int CLOSE = 6;
/*     */   public static final int RECT = 7;
/*     */   private int operation;
/*     */   private List<Float> segmentData;
/*     */   private Matrix ctm;
/*     */   
/*     */   public PathConstructionRenderInfo(int operation, List<Float> segmentData, Matrix ctm) {
/* 101 */     this.operation = operation;
/* 102 */     this.segmentData = segmentData;
/* 103 */     this.ctm = ctm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathConstructionRenderInfo(int operation, Matrix ctm) {
/* 110 */     this(operation, null, ctm);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOperation() {
/* 117 */     return this.operation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Float> getSegmentData() {
/* 126 */     return this.segmentData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix getCtm() {
/* 133 */     return this.ctm;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/PathConstructionRenderInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */