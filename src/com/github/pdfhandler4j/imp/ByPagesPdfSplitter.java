/*    */ package com.github.pdfhandler4j.imp;
/*    */ 
/*    */ import com.github.pdfhandler4j.IPagesSlice;
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
/*    */ public class ByPagesPdfSplitter
/*    */   extends ByVolumePdfSplitter
/*    */ {
/*    */   public ByPagesPdfSplitter(DefaultPagesSlice... ranges) {
/* 35 */     super(ranges);
/*    */   }
/*    */ 
/*    */   
/*    */   public long combinedStart(IPagesSlice range) {
/* 40 */     return range.start();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mustSplit(long currentPageNumber, IPagesSlice slice, long max, long totalPages) {
/* 45 */     return (currentPageNumber > slice.end());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean breakOnSplit() {
/* 50 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/ByPagesPdfSplitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */