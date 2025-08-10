/*    */ package com.itextpdf.text.pdf.parser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GlyphRenderListener
/*    */   implements RenderListener
/*    */ {
/*    */   private final RenderListener delegate;
/*    */   
/*    */   public GlyphRenderListener(RenderListener delegate) {
/* 52 */     this.delegate = delegate;
/*    */   }
/*    */   
/*    */   public void beginTextBlock() {
/* 56 */     this.delegate.beginTextBlock();
/*    */   }
/*    */   
/*    */   public void renderText(TextRenderInfo renderInfo) {
/* 60 */     for (TextRenderInfo glyphInfo : renderInfo.getCharacterRenderInfos())
/* 61 */       this.delegate.renderText(glyphInfo); 
/*    */   }
/*    */   
/*    */   public void endTextBlock() {
/* 65 */     this.delegate.endTextBlock();
/*    */   }
/*    */   
/*    */   public void renderImage(ImageRenderInfo renderInfo) {
/* 69 */     this.delegate.renderImage(renderInfo);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/GlyphRenderListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */