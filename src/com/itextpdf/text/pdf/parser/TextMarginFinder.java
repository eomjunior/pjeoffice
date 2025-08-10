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
/*     */ public class TextMarginFinder
/*     */   implements RenderListener
/*     */ {
/*  53 */   private Rectangle2D.Float textRectangle = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderText(TextRenderInfo renderInfo) {
/*  62 */     if (this.textRectangle == null) {
/*  63 */       this.textRectangle = renderInfo.getDescentLine().getBoundingRectange();
/*     */     } else {
/*  65 */       this.textRectangle.add((Rectangle2D)renderInfo.getDescentLine().getBoundingRectange());
/*     */     } 
/*  67 */     this.textRectangle.add((Rectangle2D)renderInfo.getAscentLine().getBoundingRectange());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getLlx() {
/*  76 */     return this.textRectangle.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getLly() {
/*  84 */     return this.textRectangle.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getUrx() {
/*  92 */     return this.textRectangle.x + this.textRectangle.width;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getUry() {
/* 100 */     return this.textRectangle.y + this.textRectangle.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWidth() {
/* 108 */     return this.textRectangle.width;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getHeight() {
/* 116 */     return this.textRectangle.height;
/*     */   }
/*     */   
/*     */   public void beginTextBlock() {}
/*     */   
/*     */   public void endTextBlock() {}
/*     */   
/*     */   public void renderImage(ImageRenderInfo renderInfo) {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/TextMarginFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */