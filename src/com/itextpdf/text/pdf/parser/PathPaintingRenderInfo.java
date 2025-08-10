/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathPaintingRenderInfo
/*     */ {
/*     */   public static final int NONZERO_WINDING_RULE = 1;
/*     */   public static final int EVEN_ODD_RULE = 2;
/*     */   public static final int NO_OP = 0;
/*     */   public static final int STROKE = 1;
/*     */   public static final int FILL = 2;
/*     */   private int operation;
/*     */   private int rule;
/*     */   private GraphicsState gs;
/*     */   
/*     */   public PathPaintingRenderInfo(int operation, int rule, GraphicsState gs) {
/* 101 */     this.operation = operation;
/* 102 */     this.rule = rule;
/* 103 */     this.gs = gs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathPaintingRenderInfo(int operation, GraphicsState gs) {
/* 113 */     this(operation, 1, gs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOperation() {
/* 121 */     return this.operation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRule() {
/* 128 */     return this.rule;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix getCtm() {
/* 135 */     return this.gs.ctm;
/*     */   }
/*     */   
/*     */   public float getLineWidth() {
/* 139 */     return this.gs.getLineWidth();
/*     */   }
/*     */   
/*     */   public int getLineCapStyle() {
/* 143 */     return this.gs.getLineCapStyle();
/*     */   }
/*     */   
/*     */   public int getLineJoinStyle() {
/* 147 */     return this.gs.getLineJoinStyle();
/*     */   }
/*     */   
/*     */   public float getMiterLimit() {
/* 151 */     return this.gs.getMiterLimit();
/*     */   }
/*     */   
/*     */   public LineDashPattern getLineDashPattern() {
/* 155 */     return this.gs.getLineDashPattern();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/PathPaintingRenderInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */