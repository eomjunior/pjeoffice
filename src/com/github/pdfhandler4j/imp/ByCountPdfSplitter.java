/*    */ package com.github.pdfhandler4j.imp;
/*    */ 
/*    */ import com.github.pdfhandler4j.IPagesSlice;
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ public class ByCountPdfSplitter
/*    */   extends ByVolumePdfSplitter
/*    */ {
/*    */   private final long pageCount;
/*    */   
/*    */   public ByCountPdfSplitter(long pageCount) {
/* 38 */     this.pageCount = Args.requirePositive(pageCount, "pageCount is < 1");
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mustSplit(long currentCombined, IPagesSlice slice, long maxIncrement, long totalPages) {
/* 43 */     return (currentCombined >= this.pageCount);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/ByCountPdfSplitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */