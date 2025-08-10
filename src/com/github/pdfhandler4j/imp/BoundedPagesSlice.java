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
/*    */ public final class BoundedPagesSlice
/*    */   extends PagesSliceWrapper
/*    */ {
/*    */   private final long totalPages;
/*    */   
/*    */   public BoundedPagesSlice(IPagesSlice slice, long totalPages) {
/* 38 */     super(slice);
/* 39 */     this.totalPages = Args.requirePositive(totalPages, "totalPags <= 0");
/*    */   }
/*    */ 
/*    */   
/*    */   public final long start() {
/* 44 */     return Math.min(Math.max(1L, super.start()), this.totalPages);
/*    */   }
/*    */ 
/*    */   
/*    */   public final long end() {
/* 49 */     return Math.min(Math.max(1L, super.end()), this.totalPages);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/BoundedPagesSlice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */