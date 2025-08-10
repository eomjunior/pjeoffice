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
/*    */ public class GlyphTextRenderListener
/*    */   extends GlyphRenderListener
/*    */   implements TextExtractionStrategy
/*    */ {
/*    */   private final TextExtractionStrategy delegate;
/*    */   
/*    */   public GlyphTextRenderListener(TextExtractionStrategy delegate) {
/* 51 */     super(delegate);
/* 52 */     this.delegate = delegate;
/*    */   }
/*    */   
/*    */   public String getResultantText() {
/* 56 */     return this.delegate.getResultantText();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/GlyphTextRenderListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */