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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FilteredTextRenderListener
/*    */   extends FilteredRenderListener
/*    */   implements TextExtractionStrategy
/*    */ {
/*    */   private final TextExtractionStrategy delegate;
/*    */   
/*    */   public FilteredTextRenderListener(TextExtractionStrategy delegate, RenderFilter... filters) {
/* 62 */     super(delegate, filters);
/* 63 */     this.delegate = delegate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getResultantText() {
/* 71 */     return this.delegate.getResultantText();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/FilteredTextRenderListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */