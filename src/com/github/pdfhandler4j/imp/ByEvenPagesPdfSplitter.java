/*    */ package com.github.pdfhandler4j.imp;
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
/*    */ public class ByEvenPagesPdfSplitter
/*    */   extends ByParityPdfSplitter
/*    */ {
/*    */   public ByEvenPagesPdfSplitter() {
/* 32 */     super(2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected final String computeFileName(String originalName, long beginPage) {
/* 37 */     return originalName + " (PÁGINAS PARES)";
/*    */   }
/*    */ 
/*    */   
/*    */   protected final int getEndReference(int totalPages) {
/* 42 */     return isOdd(totalPages) ? --totalPages : totalPages;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/ByEvenPagesPdfSplitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */