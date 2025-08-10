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
/*     */ public class FilteredRenderListener
/*     */   implements RenderListener
/*     */ {
/*     */   private final RenderListener delegate;
/*     */   private final RenderFilter[] filters;
/*     */   
/*     */   public FilteredRenderListener(RenderListener delegate, RenderFilter... filters) {
/*  64 */     this.delegate = delegate;
/*  65 */     this.filters = filters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderText(TextRenderInfo renderInfo) {
/*  74 */     for (RenderFilter filter : this.filters) {
/*  75 */       if (!filter.allowText(renderInfo))
/*     */         return; 
/*     */     } 
/*  78 */     this.delegate.renderText(renderInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beginTextBlock() {
/*  86 */     this.delegate.beginTextBlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endTextBlock() {
/*  94 */     this.delegate.endTextBlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderImage(ImageRenderInfo renderInfo) {
/* 103 */     for (RenderFilter filter : this.filters) {
/* 104 */       if (!filter.allowImage(renderInfo))
/*     */         return; 
/*     */     } 
/* 107 */     this.delegate.renderImage(renderInfo);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/FilteredRenderListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */