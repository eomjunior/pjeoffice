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
/*    */ public abstract class ByParityPdfSplitter
/*    */   extends AbstractPdfSplitter
/*    */ {
/*    */   protected static boolean isEven(int value) {
/* 35 */     return (value % 2 == 0);
/*    */   }
/*    */   
/*    */   protected static boolean isOdd(int value) {
/* 39 */     return !isEven(value);
/*    */   }
/*    */   
/*    */   protected ByParityPdfSplitter(int startPage) {
/* 43 */     super(new IPagesSlice[] { new DefaultPagesSlice(startPage, 2147483647L) });
/*    */   }
/*    */ 
/*    */   
/*    */   protected final long nextIncrement() {
/* 48 */     return 2L;
/*    */   }
/*    */ 
/*    */   
/*    */   protected final boolean mustSplit(long currentCombined, IPagesSlice slice, long maxIncrement, long totalPages) {
/* 53 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/ByParityPdfSplitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */