/*    */ package com.itextpdf.text.pdf.languages;
/*    */ 
/*    */ import com.itextpdf.text.pdf.BidiLine;
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
/*    */ public class HebrewProcessor
/*    */   implements LanguageProcessor
/*    */ {
/* 50 */   protected int runDirection = 3;
/*    */ 
/*    */   
/*    */   public HebrewProcessor() {}
/*    */   
/*    */   public HebrewProcessor(int runDirection) {
/* 56 */     this.runDirection = runDirection;
/*    */   }
/*    */   
/*    */   public String process(String s) {
/* 60 */     return BidiLine.processLTR(s, this.runDirection, 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isRTL() {
/* 69 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/languages/HebrewProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */