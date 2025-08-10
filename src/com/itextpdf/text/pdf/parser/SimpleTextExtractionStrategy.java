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
/*     */ public class SimpleTextExtractionStrategy
/*     */   implements TextExtractionStrategy
/*     */ {
/*     */   private Vector lastStart;
/*     */   private Vector lastEnd;
/*  66 */   private final StringBuffer result = new StringBuffer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beginTextBlock() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endTextBlock() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResultantText() {
/*  91 */     return this.result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void appendTextChunk(CharSequence text) {
/* 101 */     this.result.append(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderText(TextRenderInfo renderInfo) {
/* 109 */     boolean firstRender = (this.result.length() == 0);
/* 110 */     boolean hardReturn = false;
/*     */     
/* 112 */     LineSegment segment = renderInfo.getBaseline();
/* 113 */     Vector start = segment.getStartPoint();
/* 114 */     Vector end = segment.getEndPoint();
/*     */     
/* 116 */     if (!firstRender) {
/* 117 */       Vector x0 = start;
/* 118 */       Vector x1 = this.lastStart;
/* 119 */       Vector x2 = this.lastEnd;
/*     */ 
/*     */       
/* 122 */       float dist = x2.subtract(x1).cross(x1.subtract(x0)).lengthSquared() / x2.subtract(x1).lengthSquared();
/*     */       
/* 124 */       float sameLineThreshold = 1.0F;
/* 125 */       if (dist > sameLineThreshold) {
/* 126 */         hardReturn = true;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 132 */     if (hardReturn) {
/*     */       
/* 134 */       appendTextChunk("\n");
/* 135 */     } else if (!firstRender && 
/* 136 */       this.result.charAt(this.result.length() - 1) != ' ' && renderInfo.getText().length() > 0 && renderInfo.getText().charAt(0) != ' ') {
/* 137 */       float spacing = this.lastEnd.subtract(start).length();
/* 138 */       if (spacing > renderInfo.getSingleSpaceWidth() / 2.0F) {
/* 139 */         appendTextChunk(" ");
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     appendTextChunk(renderInfo.getText());
/*     */     
/* 150 */     this.lastStart = start;
/* 151 */     this.lastEnd = end;
/*     */   }
/*     */   
/*     */   public void renderImage(ImageRenderInfo renderInfo) {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/SimpleTextExtractionStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */