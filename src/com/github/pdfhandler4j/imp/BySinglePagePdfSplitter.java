/*    */ package com.github.pdfhandler4j.imp;
/*    */ 
/*    */ import com.github.utils4j.imp.Strings;
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
/*    */ public class BySinglePagePdfSplitter
/*    */   extends ByCountPdfSplitter
/*    */ {
/*    */   public BySinglePagePdfSplitter() {
/* 34 */     super(1L);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String computeFileName(String originalName, long beginPage) {
/* 39 */     return originalName + " pg-" + Strings.padStart(beginPage, 5);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/BySinglePagePdfSplitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */