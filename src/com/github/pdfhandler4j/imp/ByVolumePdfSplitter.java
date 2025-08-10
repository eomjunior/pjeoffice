/*    */ package com.github.pdfhandler4j.imp;
/*    */ 
/*    */ import com.github.pdfhandler4j.IPagesSlice;
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
/*    */ public abstract class ByVolumePdfSplitter
/*    */   extends AbstractPdfSplitter
/*    */ {
/* 34 */   private int currentVolume = 1;
/*    */   
/*    */   protected ByVolumePdfSplitter() {}
/*    */   
/*    */   protected ByVolumePdfSplitter(DefaultPagesSlice... ranges) {
/* 39 */     super((IPagesSlice[])ranges);
/*    */   }
/*    */ 
/*    */   
/*    */   public void reset() {
/* 44 */     this.currentVolume = 1;
/* 45 */     super.reset();
/*    */   }
/*    */ 
/*    */   
/*    */   protected String computeFileName(String originalName, long beginPage) {
/* 50 */     return originalName + "_VOLUME-" + Strings.padStart(this.currentVolume++, 2) + " (pg-" + beginPage + ")";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/ByVolumePdfSplitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */