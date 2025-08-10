/*    */ package com.github.pdfhandler4j.imp;
/*    */ 
/*    */ import com.github.pdfhandler4j.IPagesSlice;
/*    */ import com.github.utils4j.imp.Args;
/*    */ 
/*    */ class PagesSliceWrapper
/*    */   implements IPagesSlice {
/*    */   private IPagesSlice slice;
/*    */   
/*    */   protected PagesSliceWrapper(IPagesSlice slice) {
/* 11 */     this.slice = (IPagesSlice)Args.requireNonNull(slice, "slice is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public long start() {
/* 16 */     return this.slice.start();
/*    */   }
/*    */ 
/*    */   
/*    */   public long end() {
/* 21 */     return this.slice.end();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/PagesSliceWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */